import Foundation
import Sodium
import Encoding
import BigInt

// open libsodium to get access to ed25519 operations
let sodium = Sodium()

// Declare a Hedera(tm) operator that will (eventually) pay
// for the message submission

let hederaOperatorNum = UInt64(ProcessInfo.processInfo.environment["OPERATOR_NUM"]!)!

// Get the owner key for the contract
// This will let us use the Mint transaction

let ownerKeyHex = ProcessInfo.processInfo.environment["OWNER_KEY"]!
let ownerKeyBytes = Array<UInt8>(hexString: ownerKeyHex.suffix(64))!
let ownerKey = sodium.sign.keyPair(seed: ownerKeyBytes)!

// Generate a timestamp with some jitter to account for differences
// to the server

// seconds -> nanoseconds
let validStartNanos = UInt64((Date().timeIntervalSince1970 * 1000000000.0).rounded())

// Generate a random ~address~ to mint our token to
let newKey = sodium.sign.keyPair()!
let newAddress = newKey.publicKey

// We are going to mint token #8
let newTokenId = BigInt(8)

// prepare the function body protobuf
let functionBody = try! Hedera721_FunctionBody.with {
    $0.caller = Data(ownerKey.publicKey)
    $0.operatorAccountNum = hederaOperatorNum
    $0.validStartNanos = validStartNanos
    $0.mint = Hedera721_MintFunctionData.with {
        $0.id = Data(serializeJavaBigInt(newTokenId))
        $0.to = Data(newAddress)
    }
}.serializedData()

// sign the function body
let functionSignature = sodium.sign.sign(message: Bytes(functionBody), secretKey: ownerKey.secretKey)!

// prepare and encode the function protobuf
let function = try! Hedera721_Function.with {
    $0.signature = Data(functionSignature)
    $0.body = functionBody
}.serializedData()

// print out the transaction as a hex string
print(Bytes(function).hexString)

// helper to serialize a BigInt as Java expects
func serializeJavaBigInt(_ value: BigInt) -> Bytes {
    var array = Array(BigUInt.init(value.magnitude).serialize())

    if array.count > 0 {
        if value.sign == BigInt.Sign.plus {
            if array[0] >= 128 {
                array.insert(0, at: 0)
            }
        } else if value.sign == BigInt.Sign.minus {
            if array[0] <= 127 {
                array.insert(255, at: 0)
            }
        }
    }

    return array
}