# DotaMatch

Introduction
------------
Welcome to our repository for CS4513-Software Engineering Spring'16 and CS4523-Design Project Fall'16. All team members working on this project can be found in the section below. 

DotaMatch is a matchmaking application developed for Android devices for the MOBA (multiplayer online battle arena) Dota 2. Our motivation for DotaMatch is to allow users to be matched with players of their choice rather than randomly being placed onto a team by Dota 2's in-game matchmaking system. The overall goal of DotaMatch, is to make the process of matchmaking a more enjoyable experience by introducing other factors besides skill level (ELO) into the matchmaking algorithm.

DotaMatch was developed on Android Studio 2.2.3 and incorporates Google's Firebase SDK. Services implemented in this project include Firebase Registration & Authentication, Real-time Database, and Cloud-Messaging (FCM). 

This repository contains the source code for our DotaMatch application. It also contains all documentation that can be referenced through the Documentation folder. 

Team Members
-------------
  
Corey Chong
  
Albert Su
  
Evans Yeung


Installation
------------
1. Requires Android Studio to run
2. Update all Android SDK before running
3. Create an Android Virtual Machine with API 24 or lower
  * DotaMatch was developed on Nexus 5 API 24
  * Testing has been conducted on Nexus 5 and 6P on API 24, 23, and 22
4. Once the VM starts running our application will automatically start.

Instructions
------------

#### Registration & Authentication

![Alt text](/README_Images/Registration&LoginFunctions.gif?raw=true)

* Email:  requires an email input
* Password: requires a minimum of 6 chars
* Account Setup
 * DotaName:  your Dota 2 in-game name
 * MMR: your Dota 2 matchmaking rating (ELO) if you play rank games
 * Role: the role you prefer or play most often in Dota 2
 * Newly created accounts will automatically have a rating of 5

Once registration has been complete, you will have access to all DotaMatch's functionalities. 

#### Party Matchmaking

![Alt text](/README_Images/PartyFunction.gif?raw=true)

* Rating: select the minimum rating of players you want to get matched with
* Player Number: select the amount of players you want our matchmaking algorithm to search for
* Search Button: initiate matchmaking
* Start Button: start the game sessions once satisfied with party
* Time Remaining: countdown timer for average Dota 2 game lenght
 * Timer set to 10 seconds for demonstration purposes
 * Once game has ended party members can rate one another by clicking on the rating bar
* Submit Button: save and update existing ratings


#### Chat/Messaging System

![Alt text](/README_Images/ChatFunction.gif?raw=true)

* All user created public chatrooms are listed
* Add Room button: any user can create a chat room
* While inside a chat room, users can input their messages which can be read by all users
