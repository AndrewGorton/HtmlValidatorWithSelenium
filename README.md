HtmlValidatorWithSelenium
=========================

Use Selenium to grab a page source and then runs the result through http://validator.w3.org.
 
Build
=====

* Maven 3.1.1
* Oracle's jdk1.8.0_20.jdk
* PhantomJS 1.9.7

```
$ mvn package
```

Run
===

Pass the list of URLs to check as parameters. Each is checked in turn.

```
$ java -jar target/HtmlValidatorWithSelenium-2.0.0-SNAPSHOT.jar <url> [<url>]
```

Returns 0 on no errors, or 1 if the total number of errors is above 0. Returns -100 on an internal error. HTML warnings are ignored.


