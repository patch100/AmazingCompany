# AmazingCompany
API created for the Amazing Company for managing Nodes and their descendants

Build with
`docker build -t crumpledhat/the-amazing-node-company .`

Run container with
`docker run -p 8080:8080 -t crumpledhat/the-amazing-node-company`

API Documentation

An example of a single node
```
{
    "id": 1,
    "height": 0,
    "parent": null,
    "root": 1,
    "children": [
        2,
        3,
        4
    ]
}
```

Any time multiple nodes are returned, paging is used and nodes will be found inside "content"
```
{
    "content": [
        {
            "id": 1,
            "height": 0,
            "parent": null,
            "root": 1,
            "children": [
                2,
                3,
                4
            ]
        },
        {
            "id": 2,
            "height": 1,
            "parent": 1,
            "root": 1,
            "children": []
        }
    ]
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


