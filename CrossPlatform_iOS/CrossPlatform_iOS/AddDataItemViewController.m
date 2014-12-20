//
//  AddDataItemViewController.m
//  CrossPlatform_iOS
//
//  Created by vAesthetic on 12/6/14.
//  Copyright (c) 2014 medelnant. All rights reserved.
//

#import "AddDataItemViewController.h"
#import "AppDelegate.h"

@interface AddDataItemViewController ()

@end

@implementation AddDataItemViewController

- (void)viewDidLoad {
    [super viewDidLoad];

    //Set title
    self.title = @"DataList";
    
    //If editObject is = nil
    if(_dataItemObject != nil) {
        
        NSString * dataTitleText = _dataItemObject[@"title"];
        NSNumber * dataQuantityNumber = _dataItemObject[@"quantity"];
        
        self.title = dataTitleText;
        self.descriptionLabel.text = @"Edit the following values for your data item";
        
        self.dataItemTitle.text = dataTitleText;
        self.dataItemQuantity.text = [NSString stringWithFormat:@"%@", dataQuantityNumber];
        [[self saveDataButton]setTitle:@"Update" forState:UIControlStateNormal];
        
    }
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)saveDataItem:(id)sender {
    NSLog(@"Save Item Clicked!");
    
    AppDelegate *appDelegate = [[UIApplication sharedApplication] delegate];
    
    if(appDelegate.isNetworkAvailable) {
        //Capture text from textfields and store within NSString pointers
        NSString    * dataTitleText         = [self.dataItemTitle.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
        NSString    * dataQuantityText      = [self.dataItemQuantity.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
        
        NSNumberFormatter * numFormatter = [[NSNumberFormatter alloc] init];
        [numFormatter setNumberStyle:NSNumberFormatterNoStyle];
        NSNumber   * dataQuantityInteger   = [numFormatter numberFromString:dataQuantityText];
        
        //If new object instantiate with appropriate class to save
        if(_dataItemObject == nil) {
            _dataItemObject = [PFObject objectWithClassName:@"DataItem"];
        }
        
        //Set Data values for object
        _dataItemObject[@"title"] = dataTitleText;
        _dataItemObject[@"quantity"] = dataQuantityInteger;
        _dataItemObject[@"user"] = [PFUser currentUser];
        
        
        //Save object to Parse DB
        [_dataItemObject saveInBackgroundWithBlock:^(BOOL succeeded, NSError *error) {
            if(succeeded) {
                
                //Send user to logged in application state
                [self performSegueWithIdentifier:@"returnToDataList" sender:nil];
                
            } else {
                //Build Alert with error messaging
                UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Error Logging In"
                                                                    message:[[error userInfo] objectForKey:@"error"]
                                                                   delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
                //Display Alert
                [alertView show];
            }
        }];
    } else {
        [self showNoNetworkMessage];
    }
    
}

//Dismiss keyboard when touches occur outside of keyboard
-(void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event {
    [self.view endEditing:YES];
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    [textField resignFirstResponder];
    return NO;
}

- (void)showNoNetworkMessage {
    UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Network not available"
                                                        message:@"Please try again later or check your network settings"
                                                       delegate:nil cancelButtonTitle:@"Close" otherButtonTitles:nil];
    //Display Alert
    [alertView show];
}

@end
