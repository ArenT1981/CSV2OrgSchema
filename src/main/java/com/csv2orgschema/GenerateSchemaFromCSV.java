// Filename: GenerateSchemaFromCSV.java
// Author: Aren Tyr (aren.unix@yandex.com)
// Date: 2020-10-19
// Version: 0.2
//
// =============================================================================
//
// Parse the CSV file and populate the storage object with the necessary values.
//
// =============================================================================

package com.csv2orgschema;

import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import java.io.FileReader;
import java.io.IOException;

import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.exceptions.CsvValidationException;

public class GenerateSchemaFromCSV
{
    // field type has been lowercased
    // switch return values follows mapping
    // listed in enum in OrgSchemaItem.java
    public static int getFieldType(String fieldType)
    {
        switch(fieldType)
        {
            case "auto_number":
                return 0;
            case "autonumber":
                return 0;
            case "auto-number":
                return 0;
            case "auto number":
                return 0;
            case "checkbox":
                return 1;
            case "currency":
                return 2;
            case "date":
                return 3;
            case "datetime":
                return 4;
            case "email":
                return 5;
            case "ext_lookup":
                return 6;
            case "extlookup":
                return 6;
            case "ext-lookup":
                return 6;
            case "ext lookup":
                return 6;
            case "formula":
                return 7;
            case "geolocation":
                return 8;
            case "location":
                return 8;
            case "lookup":
                return 9;
            case "master_detail":
                return 10;
            case "masterdetail":
                return 10;
            case "master-detail":
                return 10;
            case "master detail":
                return 10;
            case "number":
                return 11;
            case "percent":
                return 12;
            case "phone":
                return 13;
            case "picklist":
                return 14;
            case "picklist_multi":
                return 15;
            case "picklistmulti":
                return 15;
            case "picklist-multi":
                return 15;
            case "picklist multi":
                return 15;
            case "rollup":
                return 16;
            case "text":
                return 17;
            case "textarea":
                return 18;
            case "longtextarea":
                return 19;
            case "richtextarea":
                return 20;
            case "textencrypt":
                return 21;
            case "text-encrypt":
                return 21;
            case "text_encrypt":
                return 21;
            case "text encrypt":
                return 21;
            case "time":
                return 22;
            case "url":
                return 23;
            default:
                return -1;
        }
    }

    private static boolean checkRelationship(String field, String type, FieldType ft, int row, char cell)
    {
        if(field.trim().length() > 0)
        {
            if(ft != FieldType.LOOKUP && ft != FieldType.MASTER_DETAIL)
            {
                System.out.println("*** ERROR: '" + type + "' attribute cannot be set for field of type (" + ft + ").");
                System.out.println("This attribute is only valid for 'Lookup' or 'Master-Detail' types.");
                System.out.println("-> Please blank this cell (" + cell + row + ") if your field type is correct, otherwise"
                        + " please amend the field type in cell D" + row + ".");
                return false;
            }
            else
                return true;
        }
        else
        {
            try
            {
                if(ft == FieldType.LOOKUP || ft == FieldType.MASTER_DETAIL)
                    throw new CsvValidationException();
            }
            catch(CsvValidationException csv)
            {
                System.out.println("*** ERROR: When defining a 'Lookup' or 'Master Detail', the 'Relationship Name',"
                        + " 'Relationship Label' and 'Reference To' fields must all be completed.");
                System.exit(1);
            }
        }

        return false;
    }

    private static boolean checkForYes(String field)
    {
        String inputString = field.trim().toLowerCase();

        if(inputString.equals("y"))
            return true;
        else
            return false;
    }

    private static boolean checkNumericArg(String field, int type)
    {
        if(field.length() == 0) // Blank field, therefore use default value
            return false;
        else
        {
            switch(type)
            {
                case 17:
                    if(Integer.valueOf(field) <= 80)
                        return true;
                    else
                        return false;
                case 18:  // TextArea cannot have a length
                    return false;
                case 19:
                    if(Integer.valueOf(field) <= 32000 && Integer.valueOf(field) > 255)
                        return true;
                    else
                        return false;
            }

            return false;
        }

    }

    private static boolean checkLength(String field, String entity, int row, char cell)
    {
        try
        {
            if(field.length() == 0 || field == null)
            {
                System.out.println("*** ERROR: Missing " + entity + ". REQUIRED. Row " + row + ", column "
                        + cell + ", please correct.");
                System.out.println("*** ROW: " + row + " COLUMN: " + cell);
                throw new CsvValidationException();
            }
        }
        catch(CsvValidationException csv)
        {
            System.out.println("*** There is a problem with your input spreadsheet, check & correct the above cell.");
            System.exit(1);
            //csv.printStackTrace();
        }

        return true;
    }

    public static List<OrgSchemaItem> generateObjectsFromCSV(String filePath)
    {
        final boolean DEBUG = false;

        List<OrgSchemaItem> objectList = new ArrayList<OrgSchemaItem>();
        try
        {
            System.out.println("-> Processing file: " + filePath);
            CSVReaderHeaderAware inputFile = new CSVReaderHeaderAware(new FileReader(filePath));

            List<Map<String, String>> results = new ArrayList<Map<String,String>>();
            Map<String,String> line; //= new Map<String,String>();
            while ((line = inputFile.readMap()) != null)
            {
                results.add(line);
            }

            System.out.println("-> " + results.size() + " lines read in successfully from CSV.");
            //System.out.println("\t" + results.get(1).get("object"));

            Integer row = 1; // Initialise with 1. First line is header line, so effectively start at 2.

            for(Map<String,String> item: results)
            {
                row++; // Means starts with value 2, which is what we want for spreadsheet reference
                if(DEBUG == true)
                {
                    System.out.println("==== [ DEBUG: Row " + row + " ] ====");
                    System.out.println("Object | API setting: " + item.get("Object | API"));
                    System.out.println("Object | Label setting: " + item.get("Object | Label"));
                    System.out.println("Object | Description setting: " + item.get("Object | Description"));
                    System.out.println("Field Type setting: " + item.get("Field Type"));
                    System.out.println("Field | API Name setting is: " + item.get("Field | API Name"));
                    System.out.println("Field | Label setting is: " + item.get("Field | Label"));
                    System.out.println("Text | Length setting is: " + item.get("Text | Length"));
                    System.out.println("LongTextArea | Length setting is: " + item.get("LongTextArea | Length"));
                    System.out.println("Number | Scale setting is: " + item.get("Number | Scale"));
                    System.out.println("Number | Precision setting is: " + item.get("Number | Precision"));
                    System.out.println("Required setting is: " + item.get("Required"));
                    System.out.println("Unique setting is: " + item.get("Unique"));
                    System.out.println("Case Senstive setting is: " + item.get("Case Sensitive"));
                    System.out.println("Relationship Name setting is: " + item.get("Relationship Name"));
                    System.out.println("Relationship Label setting is: " + item.get("Relationship Label"));
                    System.out.println("Rerference To setting is: " + item.get("Reference To"));
                    System.out.println("Relationship Order settinig is: " + item.get("Relationship Order"));
                    System.out.println("==== [ END DEBUG ] ====");
                }

                OrgSchemaItem osi = new OrgSchemaItem();
                String fieldParse;
                System.out.println("");
                System.out.println("==== [ Setting Fields. Row: " + row + " ] ====");

                // The first 7 fields/columns are REQUIRED and must have values set
                // Use 'empty' check on first column to allow empty rows in spreadsheet
                // (for easier organisation/visual clarity in the spreadsheet)
                // 1. Process object name ============================================
                fieldParse = item.get("Object | API");
                // Ingore BLANK lines. If API field is blank, ignore the rest.
                // Therefore allow this field to be blank, assuming a blank line.
                if(fieldParse == null || fieldParse.length() == 0)
                {
                    System.out.println("*** INFO: Blank object name, therefore ignoring entire row " + row + ".");
                    System.out.println("*** INFO: Skipping row " + row + ".");
                    System.out.println("==== [ END Setting Fields. Row: " + row + " ] ====");

                    continue; // Skip forward to next iteration
                }
                else
                {
                    osi.setObjectAPIName(fieldParse);
                }

                // 2. Process object label -==========================================
                fieldParse = item.get("Object | Label");
                checkLength(fieldParse.trim(), "object label", row, 'B');
                osi.setObjectLabel(fieldParse.trim());

                // 3. Process object description =====================================
                fieldParse = item.get("Object | Description");
                checkLength(fieldParse.trim(), "object description", row, 'C');
                osi.setObjectDescription(fieldParse.trim());

                // 4. Process field type =============================================
                fieldParse = item.get("Field Type");
                int fieldType = getFieldType(fieldParse.trim().toLowerCase()); // ignore case for matching
                //System.out.println("-> Field type value: " + fieldType);

                if(fieldType == -1)
                {
                    System.out.println("*** ERROR: Unrecognised field type: '" + fieldParse + "' on row " + row
                            + ", column D" + ".");
                    throw new CsvValidationException();
                }
                else
                {
                    osi.setFieldType(fieldType);
                }

                FieldType ft = osi.getFieldType();

                // 5. Process field API name =========================================
                fieldParse = item.get("Field | API Name");
                checkLength(fieldParse.trim(), "field API name", row, 'E');
                osi.setFieldAPIName(fieldParse.trim());

                // 6. Process field label ============================================
                fieldParse = item.get("Field | Label");
                checkLength(fieldParse.trim(), "field label", row, 'F');
                osi.setFieldLabel(fieldParse.trim());

                // 7. Process field description ======================================
                fieldParse = item.get("Field | Description");
                checkLength(fieldParse.trim(), "field description", row, 'G');
                osi.setFieldDescription(fieldParse.trim());


                // 8. Process text length ============================================
                fieldParse = item.get("Text | Length");
                if(checkNumericArg(fieldParse.trim(), fieldType) == true)
                {
                    if(Integer.valueOf(fieldParse.trim()) > 0) // If zero/blank = OK, use default
                        osi.setTextLength(fieldParse.trim());
                }
                else if(fieldParse.trim().length() > 0)
                {
                    System.out.println("*** ERROR: Invalid attribute value detected.");
                    System.out.println("Check text length value for " + row + ", column"
                            + " G. Max value is 80, actual: " + fieldParse.trim());
                    if(ft != FieldType.TEXT)
                    {
                        System.out.println("*** ERROR: Mismatched attribute set for field of type (" + ft + ").");
                        System.out.println("This attribute is ONLY for Field Type: 'Text' (column D).");
                        System.out.println("-> Please blank this cell (H" + row + ") if your field type is correct, otherwise"
                                + " please amend the field type in cell D" + row + ".");
                    }
                    throw new CsvValidationException();
                }

                // 9. Process long text length =======================================
                fieldParse = item.get("LongTextArea | Length");
                if(checkNumericArg(fieldParse.trim(), fieldType) == true)
                {
                    if(Integer.valueOf(fieldParse.trim()) > 0) // If zero/blank = OK, use default
                        osi.setLongTextAreaLength(fieldParse.trim());
                }
                else if(fieldParse.trim().length() > 0)
                {
                    System.out.println("*** ERROR: Invalid attribute value detected.");
                    System.out.println("*** Check text length value for " + row + ", column H."
                            + " Max value is 32000, actual: " + fieldParse.trim());
                    if(ft != FieldType.LONGTEXTAREA)
                    {
                        System.out.println("*** ERROR: Mismatched attribute set for field of type (" + ft + ").");
                        System.out.println("This attribute is ONLY for Field Type: 'LongTextArea' (column D).");
                        System.out.println("-> Please blank this cell (I" + row + ") if your field type is correct, otherwise"
                                + " please amend the field type in cell D" + row + ".");
                    }
                    System.out.println("If length is set to less than 255, please set Field Type"
                            + " to 'TextArea' instead.");
                    System.out.println("And if the length is set to less than 80, please set Field"
                            + " Type to simply 'Text'.");
                    throw new CsvValidationException();
                }

                // 10. Process number scale ==============================================
                fieldParse = item.get("Number | Scale");
                if(fieldParse.trim().length() > 0)
                {
                    if(ft != FieldType.NUMBER)
                    {
                        System.out.println("*** ERROR: Mismatched attribute set for field of type (" + ft + ").");
                        System.out.println("This attribute is ONLY for Field Type: 'Number' (column D).");
                        System.out.println("-> Please blank this cell (J" + row + ") if your field type is correct, otherwise"
                                + " please amend the field type in cell D" + row + ".");
                        throw new CsvValidationException();
                    }

                    osi.setNumberScale(fieldParse.trim());
                }

                // 11. Process number precision  ========================================
                fieldParse = item.get("Number | Precision");
                if(fieldParse.trim().length() > 0)
                {
                    if(ft != FieldType.NUMBER)
                    {
                        System.out.println("*** ERROR: Mismatched attribute set for field of type (" + ft + ").");
                        System.out.println("This attribute is ONLY for Field Type: 'Number' (column D).");
                        System.out.println("-> Please blank this cell (K" + row + ") if your field type is correct, otherwise"
                                + " please amend the field type in cell D" + row + ".");
                        throw new CsvValidationException();
                    }

                    osi.setNumberPrecision(fieldParse.trim());
                }

                // 12. Process required attribute  ========================================
                fieldParse = item.get("Required");
                if(fieldParse.trim().length() > 0)
                {
                    if(ft == FieldType.AUTO_NUMBER || ft == FieldType.LOOKUP || ft == FieldType.MASTER_DETAIL
                            || ft == FieldType.ROLLUP || ft == FieldType.LONGTEXTAREA || ft == FieldType.TEXT_ENCRYPT)
                    {
                        System.out.println("*** ERROR: 'Required' attribute cannot be set for field of type (" + ft + ").");
                        System.out.println("This attribute is not valid for your specified field type.");
                        System.out.println("-> Please blank this cell (L" + row + ") if your field type is correct, otherwise"
                                + " please amend the field type in cell D" + row + ".");
                        throw new CsvValidationException();
                    }
                    else
                    {
                        // FIXME: Logic for checkbox type
                        if(checkForYes(fieldParse) == true)
                            osi.setRequired(true);
                        else
                            System.out.println("*** ERROR: 'Required' attribute must have a value of 'y' or 'Y' if not blank.");
                    }
                }

                // 13. Process unique attribute  ==========================================
                fieldParse = item.get("Unique");
                if(fieldParse.trim().length() > 0)
                {
                    if(ft != FieldType.TEXT && ft != FieldType.NUMBER && ft != FieldType.EMAIL)
                    {
                        System.out.println("*** ERROR: 'Unique' attribute cannot be set for field of type (" + ft + ").");
                        System.out.println("This attribute is not valid for your specified field type.");
                        System.out.println("-> Please blank this cell (M" + row + ") if your field type is correct, otherwise"
                                + " please amend the field type in cell D" + row + ".");
                        throw new CsvValidationException();
                    }
                    else
                    {
                        if(checkForYes(fieldParse) == true)
                            osi.setUnique(true);
                        else
                            System.out.println("*** ERROR: 'Unique' attribute must have a value of 'y' or 'Y' if not blank.");
                    }
                }

                // 14. Process case sensitive attribute  ==================================
                fieldParse = item.get("Case Sensitive");
                if(fieldParse.trim().length() > 0)
                {
                    if(ft != FieldType.TEXT)
                    {
                        System.out.println("*** ERROR: 'Case Sensitive' attribute cannot be set for field of type (" + ft + ").");
                        System.out.println("This attribute is only valid for 'Text' field type.");
                        System.out.println("-> Please blank this cell (N" + row + ") if your field type is correct, otherwise"
                                + " please amend the field type in cell D" + row + ".");
                        throw new CsvValidationException();
                    }
                    else
                    {
                        if(checkForYes(fieldParse) == true)
                            osi.setCaseSensitive(true);
                        else
                            System.out.println("*** ERROR: 'Case Sensitive' attribute must have a value of 'y' or 'Y' if not blank.");
                    }
                }

                // 15. Process External ID attribute  ==================================
                fieldParse = item.get("External ID");
                if(fieldParse.trim().length() > 0)
                {
                    if(ft != FieldType.TEXT && ft != FieldType.NUMBER && ft != FieldType.AUTO_NUMBER && ft != FieldType.EMAIL)
                    {
                        System.out.println("*** ERROR: 'External ID' attribute cannot be set for field of type (" + ft + ").");
                        System.out.println("This attribute is only valid for 'Text'/'[Auto-]Number'/'Email' field types.");
                        System.out.println("-> Please blank this cell (O" + row + ") if your field type is correct, otherwise"
                                + " please amend the field type in cell D" + row + ".");
                        throw new CsvValidationException();
                    }
                    else
                    {
                        if(checkForYes(fieldParse) == true)
                            osi.setCaseSensitive(true);
                        else
                            System.out.println("*** ERROR: 'External ID' attribute must have a value of 'y' or 'Y' if not blank.");
                    }
                }

                // 16. Process Default Value attribute  ==================================
                fieldParse = item.get("Default Value");
                if(fieldParse.trim().length() > 0)
                {
                    if(ft == FieldType.AUTO_NUMBER || ft == FieldType.GEOLOCATION || ft == FieldType.PICKLIST_MULTI
                            || ft == FieldType.TEXT_ENCRYPT)
                    {
                        System.out.println("*** ERROR: 'Default Value' attribute cannot be set for field of type (" + ft + ").");
                        System.out.println("This attribute is not valid for your specified field type.");
                        System.out.println("-> Please blank this cell (P" + row + ") if your field type is correct, otherwise"
                                + " please amend the field type in cell D" + row + ".");
                        throw new CsvValidationException();
                    }
                    else
                        osi.setDefaultValue(fieldParse.trim());
                }

                // 17. Process Relationship Name attribute  ==================================
                fieldParse = item.get("Relationship Name");
                if(checkRelationship(fieldParse, "Relationship Name", ft, row, 'Q') == true)
                    osi.setRelationshipName(fieldParse.trim());
                else
                    if(fieldParse.trim().length() > 0)
                        throw new CsvValidationException();


                // 18. Process Relationship Label attribute  ==================================
                fieldParse = item.get("Relationship Label");
                if(checkRelationship(fieldParse, "Relationship Label", ft, row, 'R') == true)
                    osi.setRelationshipLabel(fieldParse.trim());
                else
                    if(fieldParse.trim().length() > 0)
                        throw new CsvValidationException();

                // 19. Process Reference To attribute  ==================================
                fieldParse = item.get("Reference To");
                if(checkRelationship(fieldParse, "Reference To", ft, row, 'S') == true)
                    osi.setReferenceTo(fieldParse.trim());
                else
                    if(fieldParse.trim().length() > 0)
                        throw new CsvValidationException();

                // 20. Process Relationship Order
                fieldParse = item.get("Relationship Order");
                if(fieldParse.trim().length() > 0)
                {
                    if(ft != FieldType.MASTER_DETAIL)
                    {
                        System.out.println("*** ERROR: 'Relationship Order' attribute cannot be set for field of type (" + ft + ").");
                        System.out.println("This attribute is only valid for 'Master-Detail' field type.");
                        System.out.println("-> Please blank this cell (T" + row + ") if your field type is correct, otherwise"
                                + " please amend the field type in cell D" + row + ".");
                        throw new CsvValidationException();
                    }
                    else
                        osi.setRelationshipOrder(fieldParse.trim());
                }

                System.out.println("==== [ END Setting Fields. Row: " + row + " ] ====");
                // Add to return object list
                objectList.add(osi);
            }

            inputFile.close();

            System.out.println("");
            System.out.println("==== [ SUCCESS Processing file ] ====");

            return objectList;

        }
        catch (NullPointerException ne)
        {
            System.out.println("*** ERROR: Malformed spreadsheet? No such column.");
            ne.printStackTrace();
            System.exit(1);
        }
        catch (CsvValidationException csv)
        {
            System.out.println("There is a problem with your input spreadsheet, check & correct.");
            System.exit(1);
            //csv.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
        return objectList;
    }
}
