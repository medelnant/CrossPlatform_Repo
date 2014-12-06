//
//  DataListTableViewController.h
//  CrossPlatform_iOS
//
//  Created by vAesthetic on 12/6/14.
//  Copyright (c) 2014 medelnant. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <Parse/Parse.h>
#import <ParseUI/ParseUI.h>

@interface DataListTableViewController: PFQueryTableViewController


- (void) currentUserLogout;
- (void) addDataItem;

@end