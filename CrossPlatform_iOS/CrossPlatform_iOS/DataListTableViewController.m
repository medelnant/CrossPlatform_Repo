//
//  DataListTableViewController.m
//  CrossPlatform_iOS
//
//  Created by vAesthetic on 12/6/14.
//  Copyright (c) 2014 medelnant. All rights reserved.
//

#import "DataListTableViewController.h"
#import "AddDataItemViewController.h"
#import "AppDelegate.h"


@implementation DataListTableViewController


-(void)viewDidLoad {
    [super viewDidLoad];
    
    //Set title
    self.title = @"DataList";
    
    UIBarButtonItem *logoutItem= [[UIBarButtonItem alloc] initWithTitle:@"Logout" style:UIBarButtonItemStylePlain target:self action:@selector(currentUserLogout)];
    UIBarButtonItem *addItem = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemAdd target:self action:@selector(addDataItem)];
    
    NSArray *actionButtonItems = @[logoutItem, addItem];
    self.navigationItem.rightBarButtonItems = actionButtonItems;
    
}
-(void)loadObjects {
    
    AppDelegate *appDelegate = [[UIApplication sharedApplication] delegate];
    
    if(appDelegate.isNetworkAvailable) {
        [super loadObjects];
        [self setLoadingMessage:YES];
    } else  {
        [[self refreshControl]endRefreshing];
        [self setLoadingMessage:NO];
        [self clear];
        UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Network not available"
                                                            message:@"Please try again later or check your network settings"
                                                           delegate:nil cancelButtonTitle:@"Close" otherButtonTitles:nil];
        //Display Alert
        [alertView show];
    }
}

-(void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:YES];
    
    //Hide back button
    [self.navigationItem setHidesBackButton:YES animated:NO];
    [self.navigationItem setBackBarButtonItem:nil];
    
    //Set timer to reload/query parse db for new objects
    _reloadTimer = [NSTimer timerWithTimeInterval:20.0 target:self selector:@selector(reloadParseData:) userInfo:nil repeats:YES];
    
    NSRunLoop *loop = [NSRunLoop currentRunLoop];
    [loop addTimer:_reloadTimer forMode:NSDefaultRunLoopMode];
    
    
}



- (id)initWithCoder:(NSCoder *)aDecoder
{
    self = [super initWithCoder:aDecoder];
    if (self) {
        // This table displays items in the Todo class
        self.parseClassName = @"DataItem";
        self.textKey = @"title";
        self.pullToRefreshEnabled = YES;
        self.paginationEnabled = YES;
        self.objectsPerPage = 25;
    }
    return self;
}

- (PFQuery *)queryForTable {
    NSLog(@"QueryForTable being called");
        
    PFQuery *query = [PFQuery queryWithClassName:self.parseClassName];
    [query whereKey:@"user" equalTo:[PFUser currentUser]];
    
    // If no objects are loaded in memory, we look to the cache first to fill the table
    // and then subsequently do a query against the network.
    if (self.objects.count == 0) {
        query.cachePolicy = kPFCachePolicyCacheThenNetwork;
    }
    
    [query orderByDescending:@"createdAt"];
    
    return query;

}

- (UITableViewCell *)tableView:(UITableView *)tableView
         cellForRowAtIndexPath:(NSIndexPath *)indexPath
                        object:(PFObject *)object
{
    static NSString *cellIdentifier = @"dataListCell";
    
    PFTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:cellIdentifier];
    if (!cell) {
        cell = [[PFTableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle
                                      reuseIdentifier:cellIdentifier];
    }
    
    
    // Configure the cell to show todo item with a priority at the bottom
    cell.textLabel.text = object[@"title"];
    cell.detailTextLabel.text = [NSString stringWithFormat:@"Quantity: %@",
                                 object[@"quantity"]];
    
    return cell;
}

- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath {
    return YES;
}

- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath {
    if (editingStyle == UITableViewCellEditingStyleDelete) {
        PFObject *object = [self.objects objectAtIndex:indexPath.row];
        [object deleteInBackgroundWithBlock:^(BOOL succeeded, NSError *error) {
            [self loadObjects];
        }];
    }
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    [super tableView:tableView didSelectRowAtIndexPath:indexPath];
    _selectedObject = [self objectAtIndexPath:indexPath];
    
    //Turn off the timer
    [_reloadTimer invalidate];
    _reloadTimer = nil;
    
    [self performSegueWithIdentifier:@"addDataItem" sender:nil];
}

- (void) addDataItem {
    //Send user to logged in application state
    _selectedObject = nil;

    //Turn off the timer
    [_reloadTimer invalidate];
    _reloadTimer = nil;

    [self performSegueWithIdentifier:@"addDataItem" sender:nil];
}

- (void) currentUserLogout {
    [PFUser logOut];
    [_reloadTimer invalidate];
    _reloadTimer = nil;
    [self.navigationController popToRootViewControllerAnimated:YES];
}

-(UIStatusBarStyle)preferredStatusBarStyle
{
    return UIStatusBarStyleLightContent;
}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    if([segue.identifier isEqualToString:@"addDataItem"]) {
        AddDataItemViewController * destinationViewController = segue.destinationViewController;
        
        if(destinationViewController != nil) {
            destinationViewController.dataItemObject = _selectedObject;
        }
    }
}

- (void)setLoadingMessage:(BOOL)showHide {
    
    //Code found on stackoverflow to override loading message since
    UIActivityIndicatorViewStyle activityIndicatorViewStyle = UIActivityIndicatorViewStyleWhite;
    
    // go through all of the subviews until you find a PFLoadingView subclass
    for (UIView *subview in self.view.subviews)
    {
        if ([subview class] == NSClassFromString(@"PFLoadingView"))
        {
            // find the loading label and loading activity indicator inside the PFLoadingView subviews
            for (UIView *loadingViewSubview in subview.subviews) {
                if ([loadingViewSubview isKindOfClass:[UILabel class]])
                {
                    UILabel *label = (UILabel *)loadingViewSubview;
                    {
                        if(showHide) {
                            [label setHidden:NO];
                        } else {
                            [label setHidden:YES];
                        }
                        
                    }
                }
                
                if ([loadingViewSubview isKindOfClass:[UIActivityIndicatorView class]])
                {
                    UIActivityIndicatorView *activityIndicatorView = (UIActivityIndicatorView *)loadingViewSubview;
                    activityIndicatorView.activityIndicatorViewStyle = activityIndicatorViewStyle;
                    
                    if(showHide) {
                        [activityIndicatorView setHidden:NO];
                    } else {
                        [activityIndicatorView setHidden:YES];
                    }
                    
                }
            }
        }
    }
}

-(void)reloadParseData:(NSTimer *) timer {
    
    NSLog(@"reloadParseData Called");
    [self loadObjects];
    
}

@end



