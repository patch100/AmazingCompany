# AmazingCompany
API created for the Amazing Company for managing Nodes and their descendants

Build with
`mvnw.cmd install dockerfile:build`

Run container with
`docker run -p 8080:8080 -t springio/nodes`

API Documentation

An example of a single node
```
{
  "id":2,
  "root":1,
  "parent":1,
  "height":1,
  "children":[3,4]  
}
```

Any time multiple nodes are returned, paging is used and nodes will be found inside "content"
```
{
  "content": {
    "nodes": {
    }
  }
}
```

To page through all nodes
`GET /nodes?page=1`

To get a node by its id
`GET /nodes/1`

To get a node's descendants
`GET /nodes/1/descendants`

To update the parent of a node
`PATCH /nodes/1`

To create a child node
`POST /nodes/1/children`


