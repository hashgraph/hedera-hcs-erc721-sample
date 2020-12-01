// swift-tools-version:5.3

import PackageDescription

let package = Package(
    name: "hedera_721_example",
    products: [
        .executable(
            name: "Hedera721Example",
            targets: ["Hedera721Example"]),
    ],
    dependencies: [
        .package(name: "Encoding", url: "https://github.com/std-swift/Encoding.git", from: "3.0.0"),
        .package(name: "SwiftProtobuf", url: "https://github.com/apple/swift-protobuf.git", from: "1.13.0"),
        .package(name: "Sodium", url: "https://github.com/jedisct1/swift-sodium.git", from: "0.8.0"),
        .package(name: "BigInt", url: "https://github.com/attaswift/BigInt.git", from: "3.0.0"),
    ],
    targets: [
        .target(name: "Hedera721Example", dependencies: [
            "SwiftProtobuf",
            "Sodium",
            "Encoding",
            "BigInt"
        ]),
    ]
)
