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
    
    //Set title
    self.title = @"Login";
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)forgotPassword:(id)sender {
    NSLog(@"Forgot Password Clicked!");
    
    //Build Alert
    UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Forgot Password"
                                                        message:@"Please provide your email address to receive a reset link for your password"
                                                       delegate:self cancelButtonTitle:@"Cancel" otherButtonTitles:@"Send", nil];
    alertView.alertViewStyle = UIAlertViewStylePlainTextInput;
    alertView.tag = 99;
    
    //Display Alert
    [alertView show];
    
    
}

- (IBAction)clientLogin:(id)sender {
    NSLog(@"Login Clicked!");
    
    //Capture text from textfields and store within NSString pointers
    NSString * userNameText     = [self.username.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
    NSString * passwordText     = [self.password.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
    
    NSLog(@"%@", [NSString stringWithFormat:@"Username: %@ | Password %@", userNameText, passwordText]);
    
    //Test if fields are empty
    if([userNameText length] == 0 ||
       [passwordText length] == 0) {
        
        //Build alert
        UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Login"
                                                            message:@"Make sure you enter a username and password!"
                                                           delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
        //Display alert
        [alertView show];
    } else {
        
        NSLog(@"We're read to send to Parse");
        NSLog(@"%@", [NSString stringWithFormat:@"Username: %@ | Password: %@", userNameText, passwordText]);
        
        
        [PFUser logInWithUsernameInBackground:userNameText password:passwordText
                                        block:^(PFUser *user, NSError *error) {
                                            if (user) {
                                                
                                                //Send user to logged in state of application
                                                [self performSegueWithIdentifier:@"loginSuccess" sender:nil];
                                                
                                                //Clear Values within fields
                                                self.username.text = @"";
                                                self.password.text = @"";
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
    
    //Dismiss keyboard
    [[self username]resignFirstResponder];
    [[self password]resignFirstResponder];
    
}

- (IBAction)createAccount:(id)sender {
    NSLog(@"Create Account Clicked!");
}

- (void)alertView:(UIAlertView *)alertView willDismissWithButtonIndex:(NSInteger)buttonIndex {
    
}

- (void)alertView:(UIAlertView *)alertView didDismissWithButtonIndex:(NSInteger)buttonIndex {
    //NSLog(@"Callback Did!");
    
    NSLog(@"%@", [NSString stringWithFormat:@"Alert Button Index: %ld", (long)buttonIndex]);
    
    if(buttonIndex != 0) {
        if (alertView.tag == 99) {
            
            //Retrive text from textfield and assign to pointer
            NSString * emailAddress = [[alertView textFieldAtIndex: 0] text];
            
            [PFUser requestPasswordResetForEmailInBackground:emailAddress block:^(BOOL succeeded, NSError *error) {
                if (error) {
                    UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Sorry"
                                                                        message:[error.userInfo objectForKey:@"error"]
                                                                       delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
                    [alertView show];
                } else {
                    UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Password Reset Email Sent"
                                                                        message:@"An email will arrive shortly that will allow for you to reset your password."
                                                                       delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
                    [alertView show];
                }
            }];
            
            
        }
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

@end
