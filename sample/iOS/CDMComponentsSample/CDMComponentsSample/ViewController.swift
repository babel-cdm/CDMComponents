//
//  ViewController.swift
//  CDMComponentsSample
//
//  Created by Álvaro Dueñas González on 10/06/2019.
//  Copyright © 2019 babel. All rights reserved.
//

import UIKit
import main

class ViewController: UIViewController {
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        CDMComponents().loggerUtils.setLevel(level: .debug)
        
        let result = CDMComponents().securityUtils.storeSecure(key:"A",value:"Hello world")
        
        print(result)

        let final = CDMComponents().securityUtils.retrieveFromSecureStorage(key: "A")

        final.fold(left: { error in
            let e = error as! CommonCDMComponentsError
            print(e.id)
            return nil
        }, right: { ok in
            let o = ok as! String
            print(o)
            return nil
        })
        
    }

}

