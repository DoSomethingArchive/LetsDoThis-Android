package org.dosomething.letsdothis.network.models;
import org.dosomething.letsdothis.data.User;

/**
 * Created by toidiu on 4/16/15.
 */
public class ResponseUserList
{

   public Integer total;
   public Integer current_page;
   public Integer last_page;
   public User[]  data;

    //    {
    //        "total": 23,
    //        "per_page": 5,
    //        "current_page": 1,
    //        "last_page": 5,
    //        "from": 1,
    //        "to": 5,
    //        "data": [
    //            {
    //                "_id": "5480c950bffebc651c8b456f",
    //                    "email": "drawer_text@dosomething.org",
    //                ... the rest of this user data...
    //            },
    //            {
    //                ...
    //            }
    //        ]
    //    }
}
