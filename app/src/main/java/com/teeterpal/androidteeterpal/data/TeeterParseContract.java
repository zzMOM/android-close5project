package com.teeterpal.androidteeterpal.data;

/**
 * Define all Parse Objects (database tables)
 */
public class TeeterParseContract {

    /* Defines the table contents of the phone verification code */
    public static final class phoneVerificationEntry {
        // Table name
        public static final String PHONE_VERIFICATION_OBJECT = "PhoneVerification";
        // Fields
        public static final String PHONE_VERIFICATION_OBJECT_PHONE_NUMBER = "phoneNumber";
        public static final String PHONE_VERIFICATION_OBJECT_VERIFICATION_CODE = "verificationCode";
    }

    /* Defines the table contents of the teeter user */
    public static final class userEntry {
        // Parse UserObject
        // includes: objectId, username, password, authData, emailVerified
        // email, createdAt, updateAt, ACL
        public static final String USER_OBJECT = "UserObject";
        public static final String USER_OBJECT_USERNAME = "username";
        public static final String USER_OBJECT_PASSWORD = "password";
        // Extra field for parse UserObject
        public static final String USER_OBJECT_PHONE_FIELD = "phone";

    }

    /* Defines the table contents of the teeter event */
    public static final class eventEntry {
        // Table name
        public static final String EVENT_OBJECT = "EventObject";

        // Fields
        // latitude and longitude of pin
        public static final String EVENT_LATITUDE = "latitude";     // Automatically
        public static final String EVENT_LONGITUDE = "longitude";   // Automatically
        // Event address : full address, including name and address
        // Eg. Children's Discovery Museum of San Jose, 180 Woz Way, San Jose, CA 95110
        public static final String EVENT_ADDRESS = "address";       // Map search result
        // Event date and time
        public static final String EVENT_START_AT = "eventStartAt";   // Date and Time Picker
        public static final String EVENT_END_AT = "eventEndAt";   // Date and Time Picker
        // Event info: title, visibility, category, age, keywords, description, user id,
        public static final String EVENT_PROFILE_ON_OFF = "profile";   //ImageView click
        public static final String EVENT_TITLE = "title";                   // EditText
        public static final String EVENT_CATEGORY = "category";             // Spinner dropdown list
        public static final String EVENT_VISIBILITY = "visibility";         // Spinner dropdown list
        public static final String EVENT_AGE = "age";                       // EditText
        public static final String EVENT_KEYWORDS = "keywords";             // EditText
        public static final String EVENT_DESCRIPTION = "description";       // EditText
        public static final String EVENT_USER_ID = "userId";                // Automatically
        public static final String EVENT_MAP_IMAGE = "mapImage";

    }

    /* Defines the table contents of the event user register*/
    public static final class eventUserRegisterEntry {
        // Table name
        public static final String EVENT_USER_REGISTER_OBJECT = "EventUserRegister";

        // Fields
        // eventObjectId - objectId from EventObject table
        public static final String EVENT_USER_REGISTER_EVENT_ID = "eventObjectId";
        // userObjectId - objectId from UserObject table
        public static final String EVENT_USER_REGISTER_USER_ID = "userObjectId";
    }


    /* Defines the table contents of the event user check in*/
    public static final class eventUserCheckInEntry {
        // Table name
        public static final String EVENT_USER_CHECK_IN_OBJECT = "EventUserCheckIn";

        // Fields
        // eventObjectId - objectId from EventObject table
        public static final String EVENT_USER_CHECK_IN_EVENT_ID = "eventObjectId";
        // userObjectId - objectId from UserObject table
        public static final String EVENT_USER_CHECK_IN_USER_ID = "userObjectId";
    }

}
