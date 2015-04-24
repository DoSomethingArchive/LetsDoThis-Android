package org.dosomething.letsdothis.network.models;
import org.dosomething.letsdothis.data.User;

/**
 * Created by toidiu on 4/17/15.
 */
public class RequestUserUpdate
{
    public String email;
    public String mobile;
    public String first_name;
    public String last_name;

    public RequestUserUpdate(User user)
    {
        this.email = user.email;
        this.mobile = user.mobile;
        this.first_name = user.first_name;
        this.last_name = user.last_name;
    }

    //    {
//  /* Email address - forced to lowercase */
//        email: String,
//
//  /* Mobile phone number */
//                mobile: String,
//
//  /* Drupal ID */
//            drupal_id: Number,
//
//  /* Mailing address */
//            addr_street1: String,
//            addr_street2: String,
//            addr_city: String,
//            addr_state: String,
//            addr_zip: String,
//
//  /* Country */
//            country: String,
//
//  /* Date of birth */
//            birthdate: Date,
//
//  /* First name */
//            first_name: String,
//
//  /* Last name */
//            last_name: String,
//
//  /* Timestamps when document was created and last updated */
//            created_at: Date,
//            updated_at: Date,
//
//  /* List of campaign actions */
//            campaigns: Object Array
//            [
//        {
//        /* Campaign node ID */
//            nid: Number,
//
//        /* Report back ID */
//                    rbid: Number,
//
//        /* Sign up ID */
//                sid: Number
//        },
//        ...
//        ]
//    }
}
