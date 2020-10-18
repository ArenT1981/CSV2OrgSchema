![](./assets/logo.png)

# CSV2OrgSchema

# 1\. Synopsis

![](./assets/javacode.png)

![](./assets/generatedcode2.png)

This Java project programmatically generates an appropriate Salesforce Apex class that utilises the Metadata API to create any number of custom objects/fields purely from an input spreadsheet, thus bypassing the slow/tedious Salesforce web interface. It also improves on the [Salesforce object creator website](https://object-creator.salesforce.com/) which negates a lot of the benefit of this approach by not providing any customisation options for the specified fields; for example, the length or exact type of text field. 

The aim of this code is to provide a rapid method of completely describing a Salesforce object using any suitable and convenient software than can export CSV files. The result is an instantly deployable self-contained class that only requires the `MetadataService.cls` from the freely available [Metadata API](https://github.com/financialforcedev/apex-mdapi). Moreover, since the file/class is completely portable and fully specifies all of the objects/fields, it means the entire Salesforce org schema can be effortlessly moved between orgs and dynamically recreated as necessary simply by deploying and executing the class, or as a managed package through the [Salesforce workbench](https://workbench.developerforce.com/). Furthermore, it also takes care of setting the field permissions so that the fields are immediately useable, another frequently encountered and tedious problem encountered using many other methods/external tools which try to automate or assist with generating custom Salesforce objects.

# 2\. Status

![](./assets/generatedcode.png)

Software status is ALPHA but operational.  

What works:  

- Creation/generation of custom objects with most of the standard fields and specific options
- Object lookups and master-detail relationships are correctly handled
- Addition of fields on pre-existing standard objects
- Validation/error/sanity checking of the input CSV file

What is not yet implemented:  

- Formula fields
- Picklist fields/picklist generation

What is planned:  

- Suitable documentation on usage
- Test classes and code coverage
- A wrapper that will pre-package the generated class, together with the Metadata API class, generate an appropriate `package.xml`, and pack it into a ZIP so that everything can be instantly deployed via [Salesforce workbench](https://workbench.developerforce.com/)
- Automatic generation of an data dictionary from the schema into a nicely formatted markdown document with tables, that can easily be converted using [pandoc](https://pandoc.org/) to other desired formats
- A GUI written in JavaFX that would allow creation from scratch, or import from CSV
