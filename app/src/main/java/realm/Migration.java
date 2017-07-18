package realm;

import android.util.Log;

import io.realm.DynamicRealm;
import io.realm.FieldAttribute;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

/**
 * Created by MacBookPro13 on 2017. 7. 13..
 */

public class Migration implements RealmMigration {

    public int hashCode() {
        return Migration.class.hashCode();
    }

    public boolean equals(Object object) {
        if(object == null) {
            return false;
        }
        return object instanceof Migration;
    }
    @Override
    public void migrate(final DynamicRealm realm, long oldVersion, long newVersion) {
        // During a migration, a DynamicRealm is exposed. A DynamicRealm is an untyped variant of a normal Realm, but
        // with the same object creation and query capabilities.
        // A DynamicRealm uses Strings instead of Class references because the Classes might not even exist or have been
        // renamed.

        // Access the Realm schema in order to create, modify or delete classes and their fields.
        RealmSchema schema = realm.getSchema();

        /************************************************
         // Version 0
         class Person
         @Required
         String firstName;
         @Required
         String lastName;
         int    age;
         // Version 1
         class Person
         @Required
         String fullName;            // combine firstName and lastName into single field.
         int age;
         ************************************************/
        // Migrate from version 0 to version 1
        if (oldVersion == 0) {
            RealmObjectSchema personSchema = schema.get("UserData");

            // Combine 'firstName' and 'lastName' in a new field called 'fullName'
            try {
                if(!personSchema.hasField("bg_title")){
                    personSchema.addField("bg_title", String.class, FieldAttribute.REQUIRED);
                }
            }catch (IllegalArgumentException e){
                Log.e("Realm_Error", e.getMessage());
            }
            oldVersion++;
        }
    }
}
