//
//  LoggedInViewController.m
//  CrossPlatform_iOS
//
//  Created by vAesthetic on 12/6/14.
//  Copyright (c) 2014 medelnant. All rights reserved.
//

#import "LoggedInViewController.h"
#import <Parse/Parse.h>

@implementation LoggedInViewController

-(void)viewDidLoad {
    [super viewDidLoad];
    
    //Set title
    self.title = @"Logged In";
    
    //Hide back button
    [self.navigationItem setHidesBackButton:YES animated:YES];
    
    
    //Add logout button to navigationBar
    //Suppress undeclared selector warning
    
    UIBarButtonItem *logOutButton = [[UIBarButtonItem alloc] initWithTitle:@"Logout" style:UIBarButtonItemStylePlain target:self action:@selector(currentUserLogout)];
    self.navigationItem.rightBarButtonItem = logOutButton;
}


-(void) currentUserLogout {
    [PFUser logOut];
    [self.navigationController popToRootViewControllerAnimated:YES];
}

@end
