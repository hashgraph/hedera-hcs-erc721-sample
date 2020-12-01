Install `protoc-gen-swift`

```
brew install protoc-gen-swift
```

Generate swift bindings for protobufs

```
protoc -I ../../proto/src/main/proto --swift_out Sources/Hedera721Example ../../proto/src/main/proto/*.proto
```

Set env variables in your shell

```
export OWNER_KEY=302e___
export OPERATOR_NUM=___
```

Run the program

```
swift run
```

You will see a hex output such as:

```
0a560a20304a03c044fd16c2d9a4497af3f6ebdefcee83c976103a23b84ec51b4b5a59c310d04c1880eab1c783f5aea6163a250a20e5290192cd03d0e63432fd79f80e2ced4738d7c3f5965d39e98194be9e45cf4f120108129601ce88b4560ac366f2cf551631346ad662d0d469f3755482590e6bf4f5184fac1cc5679e8f4d63f1142e4020238d8cb343c020ecadd599496eebf551456745ba0f0a20304a03c044fd16c2d9a4497af3f6ebdefcee83c976103a23b84ec51b4b5a59c310d04c1880eab1c783f5aea6163a250a20e5290192cd03d0e63432fd79f80e2ced4738d7c3f5965d39e98194be9e45cf4f120108
```

Now run the TS project as follows

```
yarn ts-node index.ts 0a5...
```
