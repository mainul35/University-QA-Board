## YouTube preview
https://www.youtube.com/watch?v=1HTjNi27dK8

## Run with Docker

To get more information about how to run with docker, read the overview section in the official docker image page.

https://hub.docker.com/repository/docker/mainul35/university-qa-board/general

- Pull the image:
```
$ docker pull mainul35/university-qa-board:latest
```
- Run the image:
```
$ docker run -e JAVA_OPTS="-DDB_PASSWORD=postgres -DDB_URL=jdbc:postgresql://YOUR_DATABASE_SERVER_IP:DB_SERVER_PORT/qabord_db -DDB_USER=postgres -DFILE_LOCATION=~/Documents/docker_data/qaboard_data" -p 8080:8080 mainul35/university-qa-board:latest

....................................
06-Sep-2021 18:52:13.411 INFO [main] org.apache.catalina.startup.HostConfig.deployWAR Deployment of web application archive [/usr/local/tomcat/webapps/ROOT.war] has finished in [4,592] ms
06-Sep-2021 18:52:13.415 INFO [main] org.apache.coyote.AbstractProtocol.start Starting ProtocolHandler ["http-nio-8080"]
06-Sep-2021 18:52:13.426 INFO [main] org.apache.catalina.startup.Catalina.start Server startup in [4723] milliseconds

```
If you see the above lines, run the below command to see running container of our image:
```
$ sudo docker ps -a
CONTAINER ID   IMAGE                          COMMAND                  CREATED              STATUS              PORTS                                       NAMES
8685eec3b57f   mainul35/university-qa-board   "catalina.sh run"        About a minute ago   Up About a minute   0.0.0.0:8083->8080/tcp, :::8083->8080/tcp   qa_board
9b6d2a05ccd2   postgres                       "docker-entrypoint.s…"   About a minute ago   Up About a minute   0.0.0.0:5433->5432/tcp, :::5433->5432/tcp   qaboard_db
```
Notice that our application is exposed on port **8083**. Now from browser, navigate to http://localhost:8083 to get the login page.


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

## Password for all users: 
**``secret``**

## Roles and permissions of tasks

### Admin
- Creates Batch and views all batches (Managing batches was not added in demo version)
- Creates department and views all departments (Managing departs was not added in demo version)
- Sets terms and conditions for idea sharing.

### QA Manager
- Sees all the issues created by QA Managers and their status.
- Creates categories and views existing categories. (Managing catergories was not added in demo version)
- Can download backup file of database as zip.
- Can change issue status. (Closed or Solved).

### QA Co-ordinator
- Can see reports of his department activities
- Can visit his timeline for viewing all his posted ideas.
- Can post a new idea.
- Can view all ideas of his own department.
- Can view all ideas submitted by other staffs.
- Can submit an issue.
- Can view all issues submitted by himself.
- Can view attachments of an ideas, also can download those.
- Cannot post an idea if the closure date ends or share a comment if the final closure date of current category ends. 

### Student
- Can visit his timeline for viewing all his posted ideas.
- Can post a new idea.
- Can view all ideas of the department he is in.
- Cannot view staff ideas.
- Cannot post an idea if the closure date ends or share a comment if the final closure date of current category ends. 

### Attachment specifications:
- Maximum upload size is at most 25 MB.
- Supports only: PDF, Word document, images and videos.
- Attachments can be found upon clicking "View Full Post".

