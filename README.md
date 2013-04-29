Javango v0.1
=========
Javango is a Java framework for developing web apps. It is inspired by the Python/Django project. 

At the moment, it is very much a work in progress. Much of its functionality relies on the Java Restlet framework. This will change in future. 

Javango is an MVC framework. It uses a Djangoesque interpretation of the MVC pattern: Models are the data access layer of Javango, Views select which data to display to users and the Controller portion of the framework determines which view to delegate to depending on user input (i.e. the URL routing functionality).

Have a look at the 'books' sample application in src/co/gitm/books to get an idea of how the framework currently works. It has very limited functionality at this point in time: Model objects can be defined, created and retrieved from the database, but that's about it. 