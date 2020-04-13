[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

NOTE: Architecture is heavily based on [InstaFram](https://github.com/jelic98/instafram)

![InfView Demo](demo.png)

# 1. Introduction

This document displays, analyzes and defines the need for InfView. It focuses on the needs of stakeholders and describes details on how InfView meets needs.

## 1.1. The purpose

This document introduces customers and users with a software product InfView.

## 1.2. Definitions, acronyms and abbreviations

In this document we use the following acronyms and abbreviations:

* InfView - Universal information resource manager
* RQM - Model request includes user requirements and aims to achieve maximum understanding and those who will design and implement the system
* FUM - The functional model shows a set of cases of using systems and actors beyond the boundaries of the system
* IR - Information resource includes knowledge, information and data used to solve problems, as well as technology systems related to acquisition, storage and processing of data
* CRUD - Operations for creating, loading, updating and deleting data
* Import - Automatic or semi-automatic import of data or datasets between applications; [source](https://en.wikipedia.org/wiki/Import_and_export_of_data)
* Export - Automatic or semi-automatic export of data or datasets between applications; [source](https://en.wikipedia.org/wiki/Import_and_export_of_data)
* Merge - Combining two or more different versions of data; [source](https://www.techopedia.com/definition/1217/merge)
* Sorting - Systematic editing process, editing items one by one by some criterion; [source](https://en.wikipedia.org/wiki/Sorting)
* Split - Separate the data set so that each part has different features
* Cloning - The process of cloning is a method of making an exact copy of the data; [source](https://www.techopedia.com/definition/31923/cloning-programming)
* Localization - adapting a product or service to meet the needs of a particular language or culture; [source](https://searchcio.techtarget.com/definition/localization)

## 1.3.Overview

The rest of the document contains the characteristics, limitations, and documentation for the software product InfView. Also, the reader has access to the models used for software development and can install its trial version.

# 2. Positioning

## 2.1. Business Opportunity

Modern business systems use large amounts of data to help make business decisions. The growth of companies, both in the volume of revenues and in the number of business partners, is accompanied by increased data accumulation. Keeping such a data set soon becomes a problem. In a large number of cases, companies employ people who will only deal with the maintenance of these data grouped in information resources.

## 2.2. Statement of the problem

Due to the large amount of data and the nonuniform way of handling them, companies are looking for people with experience working with large information resources. Here one can notice the problem of high expenditures in the form of monthly earnings of these people. It raises the question of the necessity of employing people with experience. Handling a large amount of data presents a problem if the operations above them are not automated and generalized in terms of performance executable in different domains. For this reason, experts are required. It is necessary to solve the problem of handling a large amount of data so that operations over them abstract and approach people with no experience.

## 2.3. Statement on the solution

The solution should enable the automation of operations over information resources. Also, the solution should not be related to a specific domain, but should be used in business systems from different domains.

# 3. Overview

This section provides a high level of display of product features, interfaces for other applications, and system configurations.

## 3.1. Product Perspective

The software product InfView is independent upon use, while the InstaFram tool is required for its [installation](https://www.lazarjelic.com/ecloga/projects/infview/get.php?q=instafram). During the use of the tools, communication with external services such as user registration service, error reporting service, online help service, etc. is realized.

## 3.2. Ability Ability

The main functionality of the program InfView includes basic operations with information resources, including CRUD, Import, Export, Merge, Split and Cloning. In order for this functionality to work, it is necessary to create a meta-schema information resource that describes its structure. If the structure changes in the future, the InfView tool offers the ability to change the meta-scheme, as well as its validation and storage. The ability to change meta-schema is also useful if there are cases of using tools in close-up domains.

## 3.3. Assumptions

The program InfView can be executed on all operating systems that have the [Java Runtime](https://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html) installed.

## 3.4. Licensing and installation

Our users get a license to use InfView, which applies only to a single computer. Installing this software is done using the [InstaFram](https://www.lazarjelic.com/ecloga/projects/infview/get.php?q=instafram) tool, which is also available to our users for testing with the appropriate parameters necessary for temporary use tools.

# 4. Limitations

## 4.1. Security

The internal components of the tool must not be readable and easily accessible to the user.

## 4.2. Hiding files in a local warehouse

Each file generated by InfView should be hacked before storing to prevent the user from accessing the inside of the program.

## 4.3. Secure Authentication

Authentication should be done on the central server and should be resistant to various hacker attacks such as SQL injection and CSRF.

## 4.4. Availability

The tool should be ready for use at all times and must not be canceled.

## 4.5. Portability

The tool should run and run without any disadvantages on different platforms (MacOS, Windows, Linux), with a consistent layout of UI elements and functionality, and a different "look and feel".

## 4.6. Location

The tool should have the option localization.

## 4.7. GUI

The work environment of the tool should be presented through the graphical user interface.

## 4.8. Events Managed System

The system should respond to events beyond the boundaries of the system.

## 4.9. Interactive system

The system is based on a large amount of interaction with the user.

# 5. Documentation

This section describes the documentation that must be developed to support the successful implementation and use of the software product InfView.

## 5.1. User manual

The user has the ability to read the instructions to be accessed from the program at any time. The guide provides a detailed overview of the options that are explained step-by-step. The manual was created to explain the general purpose of the tool, while online help is recommended for specific use.

## 5.2. Online help

Online help is intended for users who did not help the instruction or use the tool for a specific case. The user has the ability to ask at any time the question whose answer is received within 24 hours. The question is asked by filling in the [form](https://www.lazarjelic.com/ecloga/projects/software_admin/help.php).

# 6. PowerDesigner models

[Requirement Model](https://github.com/jelic98/infview/blob/master/models/RequirementsModel.rqm)

[Object Oriented Model](https://github.com/jelic98/infview/blob/master/models/ObjectOrientedModel.oom)
