// Filename: OrgSchemaItem.java
// Author: Aren Tyr (aren@clouddee.com)
// Date: 2020-10-19
// Version: 0.2
//
// =============================================================================
//
// Class that defines the object to store all of the data values from the CSV
// file.
//
// =============================================================================

package com.csv2orgschema;

enum ObjectSharingModel
{
        Private, Read, ReadWrite, ReadWriteTransfer, FullAccess, ControlledByParent, ControlledByCampaign,
        ControlledByLeadOrContact
};

enum DeploymentStatus
{
        InDevelopment, Deployed
};

enum FieldType
{
        AUTO_NUMBER, CHECKBOX, CURRENCY, DATE, // AUTO_NUMBER -> 0, CHECKBOX -> 1, CURRENCY -> 2, DATE -> 3
        DATETIME, EMAIL, EXT_LOOKUP, FORMULA, // DATETIME -> 4, EMAIL -> 5, EXT_LOOKUP -> 6, FORMULA -> 7
        GEOLOCATION, LOOKUP, MASTER_DETAIL, NUMBER, // GEOLOCATION -> 8, LOOKUP -> 9, MASTER_DETAIL -> 10, NUMBER -> 11
        PERCENT, PHONE, PICKLIST, PICKLIST_MULTI, // PERCENT -> 12, PHONE -> 13, PICKLIST -> 14, PICKLIST_MULTI -> 15
        ROLLUP, TEXT, TEXTAREA, LONGTEXTAREA, // ROLLUP -> 16, TEXT -> 17, TEXTAREA -> 18, LONGTEXTAREA -> 19
        RICHTEXTAREA, TEXT_ENCRYPT, TIME, URL
}; // RICHTEXTAREA -> 20, TEXT_ENCRYPT -> 21, TIME -> 22, URL -> 23

public class OrgSchemaItem
{

        // Core object attributes
        private String objectName;
        private String description;
        private String objectLabel;

        // Enum attributes
        private ObjectSharingModel osm;
        private DeploymentStatus ds;
        private FieldType ft;

        // Member variables
        private String fieldAPIName;
        private String fieldLabel;
        private String fieldDescription;
        private String scale;
        private String precision;
        private String length;
        private String longTxtLength;
        private boolean required;
        private boolean unique;
        private boolean externalID;
        private boolean caseSensitive;
        private String defaultValue;

        // Lookups & Master-Detail
        private String relationshipName;
        private String relationshipLabel;
        private String referenceTo;
        private String relationshipOrder;

        // Constructor with sensible defaults
        public OrgSchemaItem()
        {
                // Sane defaults
                scale = "2";
                precision = "18";
                length = "80";
                longTxtLength = "32000";
                required = false;
                unique = false;
                externalID = false;
                caseSensitive = false;
                defaultValue = "";
                relationshipOrder = "0";

                osm = ObjectSharingModel.ReadWrite;
                ds = DeploymentStatus.Deployed;
        }

        // 1. Get & Set: Object API Name
        public String getObjectAPIName()
        {
                return objectName;
        }

        public void setObjectAPIName(String name)
        {
                if (SFStandardObjectList.isStandardObject(name) == false)
                {
                        objectName = name + "__c";
                        System.out.println(" * Object name: " + objectName);
                }
                else
                {
                        objectName = name;
                        System.out.println(" * Salesforce STANDARD object detected: " + objectName);
                }
        }

        // 2. Get & Set: Object Label
        public String getObjectLabel()
        {
                return objectLabel;
        }

        public void setObjectLabel(String label)
        {
                objectLabel = label;
                System.out.println("-> Object label: " + objectLabel);
        }

        // 3. Get & Set: Object Description
        public String getObjectDescription()
        {
                return description;
        }

        public void setObjectDescription(String desc)
        {
                description = desc;
                System.out.println("-> Object description: " + description);
        }

        // 4. Get & Set: Object Field Type
        public FieldType getFieldType()
        {
                return ft;
        }

        public void setFieldType(int fieldSpec)
        {
                // check valid field type // AUTO_NUMBER -> 0, CHECKBOX -> 1, CURRENCY -> 2,
                // DATE -> 3
                switch (fieldSpec) // DATETIME -> 4, EMAIL -> 5, EXT_LOOKUP -> 6, FORMULA -> 7
                { // GEOLOCATION -> 8, LOOKUP -> 9, MASTER_DETAIL -> 10, NUMBER -> 11
                  // PERCENT -> 12, PHONE -> 13, PICKLIST -> 14, PICKLIST_MULTI -> 15
                        case 0: // ROLLUP -> 16, TEXT -> 17, TEXTAREA -> 18, LONGTEXTAREA -> 19
                                ft = FieldType.AUTO_NUMBER; // RICHTEXTAREA -> 20, TEXT_ENCRYPT -> 21, TIME -> 22, URL
                                                            // -> 23
                                break;
                        case 1:
                                ft = FieldType.CHECKBOX;
                                break;
                        case 2:
                                ft = FieldType.CURRENCY;
                                break;
                        case 3:
                                ft = FieldType.DATE;
                                break;
                        case 4:
                                ft = FieldType.DATETIME;
                                break;
                        case 5:
                                ft = FieldType.EMAIL;
                                break;
                        case 6:
                                ft = FieldType.EXT_LOOKUP;
                                break;
                        case 7:
                                ft = FieldType.FORMULA;
                                break;
                        case 8:
                                ft = FieldType.GEOLOCATION;
                                break;
                        case 9:
                                ft = FieldType.LOOKUP;
                                break;
                        case 10:
                                ft = FieldType.MASTER_DETAIL;
                                break;
                        case 11:
                                ft = FieldType.NUMBER;
                                break;
                        case 12:
                                ft = FieldType.PERCENT;
                                break;
                        case 13:
                                ft = FieldType.PHONE;
                                break;
                        case 14:
                                ft = FieldType.PICKLIST;
                                break;
                        case 15:
                                ft = FieldType.PICKLIST_MULTI;
                                break;
                        case 16:
                                ft = FieldType.ROLLUP;
                                break;
                        case 17:
                                ft = FieldType.TEXT;
                                break;
                        case 18:
                                ft = FieldType.TEXTAREA;
                                break;
                        case 19:
                                ft = FieldType.LONGTEXTAREA;
                                break;
                        case 20:
                                ft = FieldType.RICHTEXTAREA;
                                break;
                        case 21:
                                ft = FieldType.TEXT_ENCRYPT;
                                break;
                        case 22:
                                ft = FieldType.TIME;
                                break;
                        case 23:
                                ft = FieldType.URL;
                                break;
                }

                System.out.println("-> Field Type: " + ft);
        }

        // 5. Get & Set: Object Field API Name
        public String getFieldAPIName()
        {
                return fieldAPIName;
        }

        public void setFieldAPIName(String fieldAPI)
        {
                fieldAPIName = fieldAPI + "__c";
                System.out.println("-> Field API name: " + fieldAPIName);
        }

        // 6. Get & Set: Object Field Label
        public String getFieldLabel()
        {
                return fieldLabel;
        }

        public void setFieldLabel(String label)
        {
                fieldLabel = label;
                System.out.println("-> Field label: " + fieldLabel);
        }

        // 7. Get & Set: Object Field Description
        public String getFieldDescription()
        {
                return fieldDescription;
        }

        public void setFieldDescription(String desc)
        {
                fieldDescription = desc;
                System.out.println("-> Field Description: " + fieldDescription);
        }

        // 8. Get & Set: Text Length
        public String getTextLength()
        {
                return length;
        }

        public void setTextLength(String txtLength)
        {
                length = txtLength;
                System.out.println("-> Text Length: " + length);
        }

        // 9. Get & Set: Long Text Area Length
        public String getLongTextAreaLength()
        {
                return length;
        }

        public void setLongTextAreaLength(String lglength)
        {
                longTxtLength = lglength;
                System.out.println("-> Long Text Length: " + longTxtLength);
        }

        // 10. Get & Set: Number Scale
        public String getNumberScale()
        {
                return scale;
        }

        public void setNumberScale(String numScale)
        {
                scale = numScale;
                System.out.println("-> Number scale: " + scale);
        }

        // 11. Get & Set: Number Precision
        public String getNumberPrecision()
        {
                return precision;
        }

        public void setNumberPrecision(String numPrec)
        {
                precision = numPrec;
                System.out.println("-> Number precision: " + precision);
        }

        // 12. Get & Set: Required
        public boolean getRequired()
        {
                return required;
        }

        public void setRequired(boolean flag)
        {
                if (flag == true)
                {
                        required = true;
                        System.out.println("-> Field required: TRUE");
                }
                else
                {
                        required = false; // Already false anyway...
                        System.out.println("-> Field required: FALSE");
                }
        }

        // 13. Get & Set: Unique
        public boolean getUnique()
        {
                return unique;
        }

        public void setUnique(boolean flag)
        {
                if (flag == true)
                {
                        required = true;
                        System.out.println("-> Field unique: TRUE");
                }
                else
                {
                        required = false; // Already false anyway...
                        System.out.println("-> Field unique: FALSE");
                }
        }

        // 14. Get & Set: Case Sensitive
        public boolean getCaseSensitive()
        {
                return unique;
        }

        public void setCaseSensitive(boolean flag)
        {
                if (flag == true)
                {
                        required = true;
                        System.out.println("-> Field case sensitive: TRUE");
                }
                else
                {
                        required = false; // Already false anyway...
                        System.out.println("-> Field sensitive: FALSE");
                }
        }

        // 15. Get & Set: External ID
        public boolean getExternalID()
        {
                return externalID;
        }

        public void setExternalID(boolean flag)
        {
                if (flag == true)
                {
                        required = true;
                        System.out.println("-> Field external ID: TRUE");
                }
                else
                {
                        required = false; // Already false anyway...
                        System.out.println("-> Field external ID: FALSE");
                }
        }

        // 16. Get & Set: Default Value
        public String getDefaultValue()
        {
                return defaultValue;
        }

        public void setDefaultValue(String defValue)
        {
                defaultValue = defValue;
                System.out.println("-> Default Value: " + defaultValue);
        }

        // 17. Get & Set: Relationship Name
        public String getRelationshipName()
        {
                return relationshipName;
        }

        public void setRelationshipName(String relName)
        {
                relationshipName = relName;
                System.out.println("-> Relationship Name: " + relName);
        }

        // 18. Get & Set: Relationship Label
        public String getRelationshipLabel()
        {
                return relationshipLabel;
        }

        public void setRelationshipLabel(String relLabel)
        {
                relationshipLabel = relLabel;
                System.out.println("-> Relationship Label: " + relationshipLabel);
        }

        // 19. Get & Set: Reference To
        public String getReferenceTo()
        {
                return referenceTo;
        }

        public void setReferenceTo(String refTo)
        {
                referenceTo = refTo + "__c";
                System.out.println("-> Reference To: " + referenceTo);
        }

        // 20. Get & Set: Relationship Order
        public String getRelationshipOrder()
        {
                return relationshipOrder;
        }

        public void setRelationshipOrder(String relOrder)
        {
                relationshipOrder = relOrder;
                System.out.println("-> Relationship Order: " + relationshipOrder);
        }

        public boolean createObjectNumberField(int length, int scale, boolean unique, boolean externalID)
        {
                // create all the number field stuff here
                return true;
        }
        /*
         * public void setObjectName(String name) { this.objectName = name; } public
         * String getObjectName() { return this.objectName; }
         * 
         * public void setObjectName(String name) { this.objectName = name; } public
         * String getObjectName() { return this.objectName; }
         * 
         * public void setObjectName(String name) { this.objectName = name; } public
         * String getObjectName() { return this.objectName; }
         * 
         * public void setObjectName(String name) { this.objectName = name; }
         */

}
