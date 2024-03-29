//
//  CreateAccountViewController.m
//  CrossPlatform_iOS
//
//  Created by vAesthetic on 12/5/14.
//  Copyright (c) 2014 medelnant. All rights reserved.
//

#import "CreateAccountViewController.h"
#import "AppDelegate.h"

@implementation CreateAccountViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    //Set title
    self.title = @"Create Account";

}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)createAccount:(id)sender {
    NSLog(@"Create Account Clicked!");
    
    AppDelegate *appDelegate = [[UIApplication sharedApplication] delegate];
    
    if(appDelegate.isNetworkAvailable) {
        NSString * userNameText = [self.username.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
        NSString * emaillAddressText = [self.emailAddress.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
        NSString * firstnameText = [self.firstname.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
        NSString * lastnameText = [self.lastname.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
        NSString * passwordText = [self.password.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
        
        if([userNameText length]        == 0 ||
           [emaillAddressText length]   == 0 ||
           [firstnameText length]       == 0 ||
           [lastnameText length]        == 0 ||
           [passwordText length]        == 0) {
            
            UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Create Account"
                                                                message:@"Make sure you enter all fields"
                                                               delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
            
            [alertView show];
            
        } else {
            
            BOOL userNameOk = NO;
            BOOL emailAddressOK = NO;
            BOOL firstNameOk = NO;
            BOOL lastNameOk = NO;
            BOOL passwordOk = NO;
            
            if([userNameText length] > 3) {userNameOk=YES;};
            if([emaillAddressText length] > 3) {emailAddressOK=YES;};
            if([firstnameText length] > 3) {firstNameOk=YES;};
            if([lastnameText length] > 3) {lastNameOk=YES;};
            if([passwordText length] > 3) {passwordOk=YES;};
            
            if(userNameOk && emailAddressOK && firstNameOk && lastNameOk && passwordOk) {
                NSLog(@"We're read to send to Parse");
                NSLog(@"%@", [NSString stringWithFormat:@"Username: %@ | Email Address: %@ | Firstname: %@ | Lastname: %@ | Password: %@", userNameText, emaillAddressText, firstnameText, lastnameText, passwordText]);
                
                PFUser *newUser = [PFUser user];
                newUser.username = userNameText;
                newUser.password = passwordText;
                newUser.email = emaillAddressText;
                
                newUser[@"firstname"] = firstnameText;
                newUser[@"lastname"] = lastnameText;
                
                [newUser signUpInBackgroundWithBlock:^(BOOL succeeded, NSError *error) {
                    if (!error) {
                        
                        //Send user to logged in application state
                        [self performSegueWithIdentifier:@"accountCreated" sender:nil];
                        
                        //Clear all fields
                        self.username.text = @"";
                        self.emailAddress.text = @"";
                        self.firstname.text = @"";
                        self.lastname.text = @"";
                        self.password.text = @"";
                        
                    } else {
                        
                        //Build Alert with error messaging
                        UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Error Logging In"
                                                                            message:[error userInfo][@"error"]
                                                                           delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
                        //Display Alert
                        [alertView show];
                    }
                }];
            
            } else {
                UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Create Account"
                                                                    message:@"Username, Email Address, Firstname, Lastname and Password are all required to be longer than 3 characters."
                                                                   delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
                
                [alertView show];
            }
            
        }
    } else {
        [self showNoNetworkMessage];
    }
    
    //Dismiss keyboard
    [[self username]resignFirstResponder];
    [[self emailAddress]resignFirstResponder];
    [[self firstname]resignFirstResponder];
    [[self lastname]resignFirstResponder];
    [[self password]resignFirstResponder];
    
}

//Dismiss keyboard when touches occur outside of keyboard
-(void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event {
    [self.view endEditing:YES];
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    [textField resignFirstResponder];
    return NO;
}

-(UIStatusBarStyle)preferredStatusBarStyle
{
    return UIStatusBarStyleLightContent;
}

- (void)showNoNetworkMessage {
    UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Network not available"
                                                        message:@"Please try again later or check your network settings"
                                                       delegate:nil cancelButtonTitle:@"Close" otherButtonTitles:nil];
    //Display Alert
    [alertView show];
}

@end
