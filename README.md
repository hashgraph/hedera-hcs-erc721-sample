# Hedera ERC-721 Equivalent Contract

## What is this?

This project is an implementation of ERC-721 using Hedera Consensus service.

## What is in this project?
* The main application which handles HCS messages and updates the state of the contract
* A database layer that keeps track of token transactions/functions
* An api to access information about the contract
* Unit testing
* Examples in Swift and Typescript

## What is a Non-Fungible Token (NFT)?
Assets that are not identical and inherently replaceable are non-fungible.  Examples of fungible assets include units of 
currency or a copy of your favorite movie.  These each have the same value and are interchangeable with other units/copies 
of the same item.

Non-fungible assets are ones that are unique and have attributes that make them difficult to swap or replace.  Examples of 
non-fungible assets are unique artwork or unique collectibles.  These can still be exchanged, but the value between exchanges may 
or may not be equal.

NFTs, then, are tokens that are inherently unique, distinguishable, and not easily replaceable/exchangeable with another NFT.

## Why ERC-721 is a standard worth using
ERC-721 is the defacto, and most widely adopted standard for NFTs.  ERC-721 includes functions that allow for 
transferring tokens between accounts, getting account balances, getting the owner of a specific token and the total 
supply of tokens available on the network, and approving that an amount of tokens can be moved from one account by a 
third party account.

## Setup

### Java
* Clone the project

* Create the database with PostgreSQL
```bash
createdb -h 127.0.0.1 -U postgres seven_twenty_one
```
* Initiate migrations
```bash
./gradlew flywayMigrate
```
* Create a personal .env file.  A sample is provided [here](https://github.com/hashgraph/hedera-hcs-erc721-sample/blob/master/.env.sample)

* Build the application
```bash
./gradlew build
```
* Run the application
```bash
java -jar contract/build/libs/contract.jar
```

## Examples

Refer to: [Examples](https://github.com/hashgraph/hedera-hcs-erc721-sample/tree/master/examples/swift)

## State API

#### Get Information

```
/
```

```json
{
    "baseURI": "",
    "name": "Fire",
    "owner": "302a300506032b6570032100ed44bebe19f73aefb65e1dcd80c429483eb8cebd63ddd6da3950725b5d8315d2",
    "symbol": "XFR",
    "totalSupply": 0
}
```

#### Get Token

```
/token/31
```

```json
{
    "owner": "302a300506032b6570032100ed44bebe19f73aefb65e1dcd80c429483eb8cebd63ddd6da3950725b5d8315d2",
    "approved": null
}
```

#### Get Account

```
/account/{address}
```

```json
{
    "balance": 2,
    "tokens": [
        "5",
        "382932"
    ]
}
```

#### Get Transactions

```
/transaction
/account/{address}/transaction
```

```json
{
    "transactions": [
        {
            "caller": "ed44bebe19f73aefb65e1dcd80c429483eb8cebd63ddd6da3950725b5d8315d2",
            "consensusAt": "2020-12-01T13:48:34.430118Z",
            "data": {
                "baseURI": "",
                "tokenName": "Fire",
                "tokenSymbol": "XFR"
            },
            "function": "constructor",
            "status": "OK"
        }
    ]
}
```

#### Get Specific Transaction

```
/transaction/{address}/{valid-start}
```

```json

{
    "caller": "ed44bebe19f73aefb65e1dcd80c429483eb8cebd63ddd6da3950725b5d8315d2",
    "consensusAt": "2020-12-01T13:48:34.430118Z",
    "data": {
        "baseURI": "",
        "tokenName": "Fire",
        "tokenSymbol": "XFR"
    },
    "function": "constructor",
    "status": "OK"
}
```


#### Get Specific Transaction's receipt

```
/transaction/{address}/{valid-start}/receipt
```

```json
{ 
    "id":"2370/1607614667206066176", 
    "caller":"302a300506032b6570032100d788efa7be5b3c25e52cbb62846181307e5a83872e03ed5ffb6739859f13c600", 
    "consensusAt":"2020-12-10T15:39:05.859711004Z", 
    "status":"OK"
}
```
