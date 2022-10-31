<div align="center"> 
  <img src="https://github.com/ddbdev/Ticket-System-Spring-Boot/blob/main/diagrams/logo.png" />
</div>

## What can you do?

The basic idea behind this project was to build a simple Ticket System API developed in the language i'm currently studying, Java, more precisely using the framework Spring. 

I used swagger to test the endpoints.

At the moment you can perform those action:

- Creating a user (When you first register an email will be sent to you based on what mail service you've declared on the .yaml file, I'm using mailgun. After that, your account will be set as GUEST, that means that you can only confirm your account using the code that you'll receive by email.)
- Confirm a user (As said before, you'll receive a UUID code via email, you need this code to confirm your account in "/confirm" endpoint.)
- Role system (Every role has his own functionality, more will be explained later.)
- Creating a ticket (As an user you can create a ticket, every ticket has a status and a category, diverse actions are performed based on status.)
- List of ticket created (User only functions, when an user create a ticket they can see every ticket created by them in the "/my-tickets" endpoint.)
- Added a function to close the ticket only for authors who created that ticket (in case the problem has been resolved before an admin response.)
- Assign a moderator to a ticket once has been replied. Once the ticket is assigned, can only be replied by that moderator unless an admin re-assign that ticket to someone else
- Every moderator will have a list of ticket by assignment, if he replied to a ticket or a ticket has been assigned to that moderator it will show up in "/manage-tickets" page, those ticket can be replied only by him.
- Moderator can set a ticket to RESOLVED, that will close the ticket. (TODO)




## Run Locally

Clone the project

```bash
  git clone https://github.com/ddbdev/Ticket-System-Spring-Boot/tree/master
```

There will be no user registered at the time you run the project, once you run, before register a new user please read the IMPORTANT message below.


IMPORTANT: Create some role in authority via DB, you can't create authority in swagger unless you're admin, set at least 1 role to default to proceed with the registration.


# Scope of the project
This project has been developed for personal purposes, if you want to use it in your project I'll be more than happy to help you if you face any problem.
If you use it a star would be appreciated.



