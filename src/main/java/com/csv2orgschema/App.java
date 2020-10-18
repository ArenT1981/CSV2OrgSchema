package com.csv2orgschema;

import java.util.List;

public class App
{
        public static void main(String[] args)
        {
                if (args.length > 0)
                {
                        System.out.println("length: " + args.length);
                        String inFile = args[0];

                        List<OrgSchemaItem> schema = GenerateSchemaFromCSV.generateObjectsFromCSV(inFile);
                        WriteApexSchema writeSchema = new WriteApexSchema(schema);
                        writeSchema.generateApexSchemaCode();
                }
                else
                {
                        System.out.println("No input file specified. Exiting.");
                }
        }
}

// TODO: Add GUI

// TODO:
// 1. Add delete/cleanup method
// 2. Work from file argument
// 3. Fix formula/blanks type
// 4. Implement picklist field
// 5. Write file headers on all source
// 6. Check for required and remove from permission set
