
### Class

<img src="http://yuml.me/diagram/scruffy/class/[Customer]" >

### Simple Association

<img src="http://yuml.me/diagram/scruffy/class/[Customer]->[Address]" >

Simple Associations
<img src="http://yuml.me/diagram/scruffy/class/[Customer]->[Address]->[AddressLine]" >

Cardinality
<img src="http://yuml.me/diagram/scruffy/class/[Customer]1-0..*[Address]" >

Directional Association
<img src="http://yuml.me/diagram/scruffy/class/[Order]-invoicing >[Address], [Order]-shipping >[Address]" >

Splash of Colour And UTF-8
<img src="http://yuml.me/diagram/scruffy/class/[✚ Thing{bg:white}]❶- ✱>[Order {bg:green}]" >

Aggregation
<img src="http://yuml.me/diagram/scruffy/class/[Company]<>-1>[Location], [Location]+->[Office]" >

Composition
<img src="http://yuml.me/diagram/scruffy/class/[Company]++-*>[Location]" >

Notes
<img src="http://yuml.me/diagram/scruffy/class/[Customer]<>1-*>[Order], [Customer]-[note: Aggregate Root {bg:cornsilk}]" >


Inheritance
<img src="http://yuml.me/diagram/scruffy/class/[Wage]^-[Salaried], [Wage]^-[Contractor]" >


Interface Inheritance
<img src="http://yuml.me/diagram/scruffy/class/[<<ITask>>]^-.-[NightlyInvoicingTask]" >

International Characters
<img src="http://yuml.me/diagram/scruffy/class/[안녕하세요|हलो;你好;مرحبا]->[Köttbullar {bg:wheat}]" >

Bold and Underline
<img src="http://yuml.me/diagram/scruffy/class/[Customer|*bold*;_italic_;not_italic|do_it()]->[Order {bg:orange}]" >


Dependencies
<img src="http://yuml.me/diagram/scruffy/class/[HttpContext]uses -.->[Request]" >


Interface
<img src="http://yuml.me/diagram/scruffy/class/[<<IDisposable>>;UnitOfWork]" >


Class with Details
<img src="http://yuml.me/diagram/scruffy/class/[User|#counter;+Forename;+Surname;+HashedPassword;-Salt|+Login(user,pass);+Logout();+Register()]" >


# Use Case
<img src="http://yuml.me/diagram/scruffy/usecase/(note: figure 1.2{bg:beige}), [App User]-(Login),[Site Maintainer]-(Add User),(Add User)<(Add Company),[Site Maintainer]-(Upload Docs),(Upload Docs)<(Manage Folders),[App User]-(Upload Docs), [App User]-(Full Text Search Docs), (Full Text Search Docs)>(Preview Doc),(Full Text Search Docs)>(Download Docs), [App User]-(Browse Docs), (Browse Docs)>(Preview Doc), (Download Docs), [Site Maintainer]-(Post New Event To The Web Site), [App User]-(View Events)" >

# Fork/Join
<img src="http://yuml.me/diagram/scruffy/activity/(start)-><a>[kettle empty]->(Fill Kettle)->|b|,<a>[kettle full]->|b|->(Boil Kettle)->|c|,|b|->(Add Tea Bag)->(Add Milk)->|c|->(Pour Water)->(end),(Pour Water)->(end)" >

# Something Meaty!
<img src="http://yuml.me/diagram/scruffy/class/[note: You can stick notes on diagrams too!{bg:wheat}],[Customer]<>1-orders 0..*>[Order], [Order]++*-*>[LineItem], [Order]-1>[DeliveryMethod], [Order]*-*>[Product], [Category]<->[Product], [DeliveryMethod]^[National], [DeliveryMethod]^[International]" >

