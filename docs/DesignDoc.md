---
geometry: margin=1in
---
# PROJECT Design Documentation

> _The following template provides the headings for your Design
> Documentation.  As you edit each section make sure you remove these
> commentary 'blockquotes'; the lines that start with a > character
> and appear in the generated PDF in italics._

## Team Information
* Team name: Fantastic Five
* Team members
  * Michael Oldziej
  * Kolbe Yang
  * Troy Wolf
  * Sherry Robinson
  * Lianna Pottgen
## Executive Summary

This is a summary of the project.

### Purpose
> _Provide a very brief statement about the project and the most
> important user group and user goals._

### Glossary and Acronyms
> _Provide a table of terms and acronyms._

| Term | Definition |
|------|------------|
| SPA | Single Page |


## Requirements

This section describes the features of the application.

> _In this section you do not need to be exhaustive and list every
> story.  Focus on top-level features from the Vision document and
> maybe Epics and critical Stories._

### Definition of MVP
The E-store for Letchworth Campgrounds handles the reservations for various campsites offered by the company. The e-store owner can login and obtain admin privileges such as being able to add, remove, and edit the campsites on the inventory which includes their names, rates, and current availability (able to be reserved or not). Customers sign up and login with their credentials. Once logged in, customers can see a list of campsites and search for a specific campsite under the home page. Customers can also create a reservation at a specific campsite and have it added to their cart, remove a reservation from their cart, and purchase all reservations currently in their cart. Once purchased, the customer can see the reservations under the Reservations page and be able to cancel them. 
Each campsite can only have one reservation at any given time, so if a user makes a reservation at one campsite, another user cannot make an overlapping reservation at the same campsite. The reservations in the customers shopping cart remain even after logging out, and once a reservation is in a shopping cart, it is considered a part of the schedule of that campsite and no other user will be able to make a reservation at that campsite that overlaps.

### MVP Features
STORY\n
Login: As an owner, I can login with the username “admin” and the password “1234” so that I can receive administrator privileges.
EPIC
Edit Inventory: As an owner, I want to edit the campsites listed in the inventory so that the list of campsites displayed  to the customers is accurate and up to date.
STORY
Add Campsite: As an owner, I want to add a new campsite to the inventory so that it will be displayed to the customers.
STORY
Remove Campsite: As an owner, I want to remove campsites from the inventory so that it will no longer be displayed in the e-store.
STORY
Create Account: As a customer, I want to create an account so I can login to the e-store.
Login: As a customer, I want to login with my credentials so that I can access the e-store and make reservations.
STORY
Create Reservation: As a customer, I can select a campsite and enter the dates for my reservation so that my reservation will be added to my cart and my spot and time will be reserved.
STORY
Remove Reservation from Cart: As a customer, I can remove a campsite from my cart so that when I decide I don’t want the reservation I don’t have to pay for it when I checkout.
STORY
Checkout: As a customer, I can checkout and purchase all reservations in my cart so that my reservations will be confirmed.
STORY
Cancel Reservation: As a customer, I can cancel a reservation I have paid for so that my spot at that campsite is no longer reserved.
STORY
Search for a Campsite: As a customer, I can search for campsites with names similar to what I search for so that I can find a specific campsite without having to browse the entire inventory.
STORY
Browse Campsites: As a customer, I can browse through all listed campsites so that I can choose one to make a reservation at.


### Roadmap of Enhancements
> _Provide a list of top-level features in the order you plan to consider them._


## Application Domain

This section describes the application domain.

![Domain Model](FantasticFiveUpdatedDomainModelSprint1-1.png)

Our overall Domain Model is a representation of the classes and proper dictation we have used throughout our project. Our Domain Model goes through the entire relation of classes as we move through all of the attributes that our project has. In order to properly explain each step of our domain model I will go class by class to explain the proper relationships and attributes behind each class.
    To begin, we start with the User class. The User class will define who exactly is logging into our application. There are two options for this, an Admin who will have special privileges within the application and the Customer who will have limited access to the website data. The User class will hold the username and password attribute. The User class will also hold a constructor attribute that will declare what kind of user this is. Our constructor will look something like: User(String username, String password, Boolean isAdmin). In our case, if the user is a customer we might have something like User user = new User(bat, man, false), so this user will have username bat, password man, and is not an admin. This User class will handle all exceptions. It will be able to see the attributes of both the admin and customer class, but no classes can see any user methods. 
Next, we will discuss the Admin class. The Admin class is a class that will be able to communicate with the Inventory class in order to remove, add, or manage products for however many times that they need or want to. The Admin class is a subclass of the User class but it cannot see the attributes or methods of the User class. Overall, this Admin class will maintain the properties of what an Admin User can do. Since the Admin class is simply a subclass of the User class, it simply inherits some simple methods like perhaps getUsername(), getPassword(), getIsAdmin(), etc…
    Next, we will discuss the Customer class. The Customer class is a class that will be able to view the data of the Inventory class. The Customer can then interact with this inventory that they can view by adding certain products to their cart. Once this Customer is also added they are given a cart. The Customer is able to add and remove products from their cart after browsing through inventory. This shopping cart will hold zero to infinity products, in our case reservations. The Customer can also take whatever is in their Shopping Cart, depending on what is in their shopping cart attribute of the shopping cart class. Once the user chooses to checkout, they will be able to place an order. The user can do this since the customer class is able to store and obtain methods and values of the order class. So overall, the Customer can browse the inventory, once they find a product of interest, they are able to add this product to their premade and username assigned shopping cart. Once in the shopping cart they are able to remove this from this cart or are able to checkout. Once the user checks out, they are able to purchase their entire cart and place the order.  
Next, we have the Order class. The Customer can see the order class and will be able to hold Order class information. The Order class is really only affected when the User orders something new. In our case, everytime the user creates and pays for a new cart with a set of reservations, it will be added to their past order array based on the Customer’s username. The Order class will contain the total price of everything in that order, the reservation data, and the data and time that the order was paid for and went through. The Customer can create 1 to infinity orders since each time they want to pay for a cart set that paid order will be put into their order list. So the Order class will be able to access the basic data from the Customer, for example username, and the Customer will be able to access all of the Order data that they would need. 
    Next, we have the Shopping cart class. The Shopping cart class will simply hold the data of whatever the customer has wanted to put in their cart in order to checkout so far. The shopping cart will hold the total price value that will hold the entire cart price ( all the reservation prices in cart added up), and will hold the reservations. Since the Shopping cart is only one per user and is saved based on the username of the Customer, there will only be one shopping cart per user. This class will be strongly reliable on the Customer class since without the Customer, there is no shopping cart. The Shopping cart will be able to hold unlimited amounts of reservations. The Customer can see the Shopping cart but the Shopping cart can’t really get the customer data.  The Shopping cart class will be able to see the Reservation class as well. 
The Inventory cart class will have access to all of the possible reservations we can have for each campsite. Since our inventory is reservation based, it needs to hold all of the possible reservation time slots for each campsite. The Inventory class is seen by both the Customer and the Admin. It is editable by the Admin and is only viewable by the Customer. The Admin is able to access and add an unlimited amount of items in the inventory so the relationship is editable and infinite. The User is only able to view the one inventory set of data so its relationship is viewable finite, 1. 
    Moving onto the Campsite class. The Campsites are one of the most basic components of our application. Because of this, the Inventory is strongly reliant on the campsites class which is why there is aggregation between the Inventory and the campsites. The inventory is able to hold 0 to infinite campsites, but the campsites can only be a part of one inventory since there is only one inventory to hold every product/reservation. It will hold the name of the campsite and the available schedule of the campsite as well in order to aid in the reservation process. The campsite will need to base its availability on the reservation class. Because of this, all reservations are really really strongly reliable on the Campsite class and could not function at all without it. As stated previously, this Campsite will be used in the inventory classe, but since there is only one inventory it can only access 1. 
Finally, the Reservation class. The Reservation class is pretty much the total package of our product. It will hold the startDay, endDay, Price, and when constructed will also hold what campsite it is associated with and the username of who placed the reservation. This class will be strongly associated with the campsites. Without the campsite there would be nothing to reserve so the campsite class must function properly in order for the reservations to function. There can only be one campsite per reservation but there can be infinitely many reservations per campsite. The Reservation class and the Campsite class can see each other since they must access the available and current reservations for each new order. 
    Overall, our domain model shows the general movement of how we process, store, and relate data in our application. 



## Architecture and Design

This section describes the application architecture.

### Summary

The following Tiers/Layers model shows a high-level view of the webapp's architecture.

![The Tiers & Layers of the Architecture](architecture-tiers-and-layers.png)

The e-store web application, is built using the Model–View–ViewModel (MVVM) architecture pattern. 

The Model stores the application data objects including any functionality to provide persistance. 

The View is the client-side SPA built with Angular utilizing HTML, CSS and TypeScript. The ViewModel provides RESTful APIs to the client (View) as well as any logic required to manipulate the data objects from the Model.

Both the ViewModel and Model are built using Java and Spring Framework. Details of the components within these tiers are supplied below.


### Overview of User Interface

This section describes the web interface flow; this is how the user views and interacts
with the e-store application.

> _Provide a summary of the application's user interface.  Describe, from
> the user's perspective, the flow of the pages in the web application._


### View Tier
> _Provide a summary of the View Tier UI of your architecture.
> Describe the types of components in the tier and describe their
> responsibilities.  This should be a narrative description, i.e. it has
> a flow or "story line" that the reader can follow._

> _You must also provide sequence diagrams as is relevant to a particular aspects 
> of the design that you are describing.  For example, in e-store you might create a 
> sequence diagram of a customer searching for an item and adding to their cart. 
> Be sure to include an relevant HTTP reuqests from the client-side to the server-side 
> to help illustrate the end-to-end flow._


### ViewModel Tier
> _Provide a summary of this tier of your architecture. This
> section will follow the same instructions that are given for the View
> Tier above._

> _At appropriate places as part of this narrative provide one or more
> static models (UML class diagrams) with some details such as critical attributes and methods._


### Model Tier
> _Provide a summary of this tier of your architecture. This
> section will follow the same instructions that are given for the View
> Tier above._

> _At appropriate places as part of this narrative provide one or more
> static models (UML class diagrams) with some details such as critical attributes and methods._

### Static Code Analysis/Design Improvements
> _Discuss design improvements that you would make if the project were
> to continue. These improvement should be based on your direct
> analysis of where there are problems in the code base which could be
> addressed with design changes, and describe those suggested design
> improvements._

> _With the results from the Static Code Analysis exercise, 
> discuss the resulting issues/metrics measurements along with your analysis
> and recommendations for further improvements. Where relevant, include 
> screenshots from the tool and/or corresponding source code that was flagged._

## Testing
> _This section will provide information about the testing performed
> and the results of the testing._

### Acceptance Testing
> _Report on the number of user stories that have passed all their
> acceptance criteria tests, the number that have some acceptance
> criteria tests failing, and the number of user stories that
> have not had any testing yet. Highlight the issues found during
> acceptance testing and if there are any concerns._

### Unit Testing and Code Coverage
> _Discuss your unit testing strategy. Report on the code coverage
> achieved from unit testing of the code base. Discuss the team's
> coverage targets, why you selected those values, and how well your
> code coverage met your targets. If there are any anomalies, discuss
> those._

Our main unit testing strategy was to flesh out the classes, and then once implemented write out tests then check the coverage using jacoco to determine what other test cases/branches need to be covered. For some classes in the model it made more sense to follow Test Driven Development and have tests the fail until methods are implemented, but for the remaining classes in the other tiers it wasn't as feasible.
