//
//  DataListTableViewController.m
//  CrossPlatform_iOS
//
//  Created by vAesthetic on 12/6/14.
//  Copyright (c) 2014 medelnant. All rights reserved.
//

#import "DataListTableViewController.h"


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

-(void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:YES];
    
    //Hide back button
    [self.navigationItem setHidesBackButton:YES animated:NO];
    [self.navigationItem setBackBarButtonItem:nil];
    
    
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

- (void) addDataItem {
    //Send user to logged in application state
    [self performSegueWithIdentifier:@"addDataItem" sender:nil];
}

- (void) currentUserLogout {
    [PFUser logOut];
    [self.navigationController popToRootViewControllerAnimated:YES];
}

-(UIStatusBarStyle)preferredStatusBarStyle
{
    return UIStatusBarStyleLightContent;
}
@end



