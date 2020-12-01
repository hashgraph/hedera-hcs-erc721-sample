# Hedera ERC-721 Equivalent Contract

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
    "balance": 2
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
