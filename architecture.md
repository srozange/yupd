# Hexagonal Architecture
```
                   [CLI Application]
                          |
                          | GitFileUpdaterParameter
                          v
                   ┌────────────────┐
                   │ GitFileUpdater │ ◄── INPUT PORT
                   └────────────────┘
                          |
                          v
              ┌─────────────────────────┐
             ╱                          ╲
            ╱         DOMAIN             ╲
           ╱      (Business Logic)        ╲
          ╱                                ╲
         ╱   • GitFile (model)              ╲
        │    • GitRepository (model)        │
        │    • GitFileUpdaterImpl (service) │
         ╲                                 ╱
          ╲                               ╱
           ╲                             ╱
            ╲                           ╱
             ╲_________________________╱
                     |        |
                     |        | 1..n ContentUpdateCriteria<yaml|regex> 
                     |        |
          ┌─────────────┐  ┌───────────────┐
          │GitConnector │  │ContentUpdater │ ◄── OUTPUT PORTS
          └─────────────┘  └───────────────┘
                 |               |
                 v               v
        ┌─────────────────┐  ┌──────────────────┐
        │   GIT ADAPTERS  │  │  UPDATE ADAPTER  │
        │                 │  │                  │
        │ • GithubConnect │  │ • ContentUpdImpl │
        │ • GitlabConnect │  │ • RegexUpdater   │
        │ • GitConnFactory│  │ • YamlPathUpdater│
        └─────────────────┘  └──────────────────┘
```