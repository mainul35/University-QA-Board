## YouTube preview
https://www.youtube.com/watch?v=1HTjNi27dK8

## Live URL
http://www.mainul35.info:8080/ewsd/

## Assumption
- Developed as a sub system of a complete University management system. Only QA Management panel has been added in here.
- A student activity is counted if a student submits an idea or comments on an idea.
- A QA Coordinator activity is counted if he submits an idea or comments on an idea or submits an issue.

## Usernames and roles:
| Username                 |       Role                                   |
| ------------------------ | -------------------------------------------- |
| admin                    |       ADMIN (Admin Dept.)                    |
| qa_manager               |     QA Manager (QA Dept.)                    |
| comp_sci_qa_coordinator  |     QA Coordinator (Computer science Dept.)  |
| tanveer                  |     QA Coordinator (Archiology Dept.)        |
| mainul35                 |     Student (Computer science Dept.)         |
| sazzad                   |     Student (Computer science Dept.)         |
| tanjina                  |     Student (Archiology Dept.)               |

## Password for all users: secret

## Roles and permissions of tasks

** Admin
- Creates Batch and views all batches (Managing batches was not added in demo version)
- Creates department and views all departments (Managing departs was not added in demo version)
- Sets terms and conditions for idea sharing.

** QA Manager
- Sees all the issues created by QA Managers and their status.
- Creates categories and views existing categories. (Managing catergories was not added in demo version)
- Can download backup file of database as zip.
- Can change issue status. (Closed or Solved).

** QA Co-ordinator
- Can see reports of his department activities
- Can visit his timeline for viewing all his posted ideas.
- Can post a new idea.
- Can view all ideas of his own department.
- Can view all ideas submitted by other staffs.
- Can submit an issue.
- Can view all issues submitted by himself.
- Can view attachments of an ideas, also can download those.
- Cannot post an idea if the closure date ends or share a comment if the final closure date of current category ends. 

** Student
- Can visit his timeline for viewing all his posted ideas.
- Can post a new idea.
- Can view all ideas of the department he is in.
- Cannot view staff ideas.
- Cannot post an idea if the closure date ends or share a comment if the final closure date of current category ends. 

** Attachment specifications:
- Maximum upload size is at most 25 MB.
- Supports only: PDF, Word document, images and videos.
- Attachments can be found upon clicking "View Full Post".

