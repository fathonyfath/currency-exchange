// swift-tool-version: 5.8

import PackageDescription

let package = Package(
    name: "CurrencyExchange", 
    products: [
        .library(
            name: "library", 
            targets: ["library"]
        ),
    ],
    targets: [
        .binaryTarget(
            name: "library", 
            url: "https://github.com/fathonyfath/currency-exchange/releases/download/v1.5.0/library.xcframework.zip",
            checksum: "272df57e43d43a0752ad766c05a66934"
        ),
    ],
)
