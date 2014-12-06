//
//  LoginViewController.h
//  CrossPlatform_iOS
//
//  Created by vAesthetic on 11/24/14.
//  Copyright (c) 2014 medelnant. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <Parse/Parse.h>

@interface LoginViewController : UIViewController <UITextFieldDelegate>

@property (weak, nonatomic) IBOutlet UITextField *username;
@property (weak, nonatomic) IBOutlet UITextField *password;

- (IBAction)forgotPassword:(id)sender;
- (IBAction)clientLogin:(id)sender;
- (IBAction)createAccount:(id)sender;

@end

