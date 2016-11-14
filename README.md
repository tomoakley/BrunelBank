# Brunel Banking System

Coursework for Brunel University CS3004 (Computer Networking). The task is to build a banking system with these features:

- It must allow customers to add and subtract money to and from their accounts
- It must allow customers to transfer money between accounts
- It must process customers’ transactions concurrently
- It must be capable of avoiding concurrency control problems (specifically lost update and inconsistent
retrieval)
- Locking should be done at the account level (e.g. an object)
- You do not need to solve deadlock problems – just show them
- The system does not need to be fault tolerant (in this demonstrator)

Your system must be implemented as a multiple client-single server system. Based on the above, your demonstrator must be capable of:
- Showing at least four clients connected to the server
- Being able to add/subtract money to individual accounts concurrently
- Being able to transfer money between pairs of accounts concurrently
- Showing how concurrency control problems are avoided
- Showing how you lock at the accounts level
- Showing at least one case of deadlock
