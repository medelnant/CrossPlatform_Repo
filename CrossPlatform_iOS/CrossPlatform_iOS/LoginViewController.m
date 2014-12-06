//
//  ViewController.m
//  CrossPlatform_iOS
//
//  Created by vAesthetic on 11/24/14.
//  Copyright (c) 2014 medelnant. All rights reserved.
//

#import "LoginViewController.h"

@interface LoginViewController ()

@end

@implementation LoginViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)forgotPassword:(id)sender {
    NSLog(@"Forgot Password Clicked!");
}

- (IBAction)clientLogin:(id)sender {
    NSLog(@"Login Clicked!");
    
    //Capture text from textfields and store within NSString pointers
    NSString * userNameText     = [self.username.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
    NSString * passwordText     = [self.password.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
    
    NSLog(@"%@", [NSString stringWithFormat:@"Username: %@ | Password %@", userNameText, passwordText]);
    
    //Test if fields are left empty
    if([userNameText length] == 0 || [passwordText length] == 0) {
        UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Oops!"
                                                            message:@"Make sure you enter a username and password!"
                                                           delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
        [alertView show];
    } else {
        NSLog(@"We're read to send to Parse");
        NSLog(@"%@", [NSString stringWithFormat:@"Username: %@ | Password: %@", userNameText, passwordText]);
        
        
        
        
        //Clear Values within fields
        self.username.text = @"";
        self.password.text = @"";
        
    }
    
}

- (IBAction)createAccount:(id)sender {
    NSLog(@"Create Account Clicked!");
}
@end
