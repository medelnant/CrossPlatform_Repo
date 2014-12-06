//
//  CreateAccountViewController.m
//  CrossPlatform_iOS
//
//  Created by vAesthetic on 12/5/14.
//  Copyright (c) 2014 medelnant. All rights reserved.
//

#import "CreateAccountViewController.h"

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
        NSLog(@"We're read to send to Parse");
        NSLog(@"%@", [NSString stringWithFormat:@"Username: %@ | Email Address: %@ | Firstname: %@ | Lastname: %@ | Password: %@", userNameText, emaillAddressText, firstnameText, lastnameText, passwordText]);
        
        
        //Clear all fields
        self.username.text = @"";
        self.emailAddress.text = @"";
        self.firstname.text = @"";
        self.lastname.text = @"";
        self.password.text = @"";
        
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


@end
