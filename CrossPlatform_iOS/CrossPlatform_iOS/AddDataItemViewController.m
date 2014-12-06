//
//  AddDataItemViewController.m
//  CrossPlatform_iOS
//
//  Created by vAesthetic on 12/6/14.
//  Copyright (c) 2014 medelnant. All rights reserved.
//

#import "AddDataItemViewController.h"

@interface AddDataItemViewController ()

@end

@implementation AddDataItemViewController

- (void)viewDidLoad {
    [super viewDidLoad];

    //Set title
    self.title = @"DataList";
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)saveDataItem:(id)sender {
    NSLog(@"Save Item Clicked!");
    
    //Capture text from textfields and store within NSString pointers
    NSString    * dataTitleText         = [self.dataItemTitle.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
    NSString    * dataQuantityText      = [self.dataItemQuantity.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
    
    NSNumberFormatter * numFormatter = [[NSNumberFormatter alloc] init];
    [numFormatter setNumberStyle:NSNumberFormatterNoStyle];
    NSNumber   * dataQuantityInteger   = [numFormatter numberFromString:dataQuantityText];
    
    PFObject *newDataItem = [PFObject objectWithClassName:@"DataItem"];
    newDataItem[@"title"] = dataTitleText;
    newDataItem[@"quantity"] = dataQuantityInteger;
    newDataItem[@"user"] = [PFUser currentUser];
    [newDataItem saveInBackgroundWithBlock:^(BOOL succeeded, NSError *error) {
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
    
    
    
}

//Dismiss keyboard when touches occur outside of keyboard
-(void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event {
    [self.view endEditing:YES];
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    [textField resignFirstResponder];
    return NO;
}

@end
