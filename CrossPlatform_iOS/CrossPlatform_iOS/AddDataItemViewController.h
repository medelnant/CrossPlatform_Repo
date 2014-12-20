//
//  AddDataItemViewController.h
//  CrossPlatform_iOS
//
//  Created by vAesthetic on 12/6/14.
//  Copyright (c) 2014 medelnant. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <Parse/Parse.h>

@interface AddDataItemViewController : UIViewController <UITextFieldDelegate>

@property (weak, nonatomic) IBOutlet UITextField *dataItemTitle;
@property (weak, nonatomic) IBOutlet UITextField *dataItemQuantity;
@property (weak, nonatomic) IBOutlet UILabel *descriptionLabel;
@property (weak, nonatomic) IBOutlet UIButton *saveDataButton;

- (IBAction)saveDataItem:(id)sender;
- (void)showNoNetworkMessage;


//Define empty pfObject to receive/set from prepareFromSegue
@property PFObject * dataItemObject;


@end
