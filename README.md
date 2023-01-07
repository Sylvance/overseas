# Overseas

OverSeas is a Ruby gem that provides access to the tracking and shipping API for UPS and DHL.

## Installation

Install the gem and add to the application's Gemfile by executing:

    $ bundle add overseas

If bundler is not being used to manage dependencies, install the gem by executing:

    $ gem install overseas

## Usage

### Tracking with UPS

```ruby
require 'overseas'

tracking_number = '1Z9999999999999999'

ups = Overseas::UPS.new
tracking_info = ups.track(tracking_number)

puts "Package status: #{tracking_info[:status]}"
```

### Shipping with DHL

```ruby
require 'overseas'

dhl = Overseas::DHL.new

shipping_info = {
  from: {
    name: 'John Doe',
    address: '123 Main Street',
    city: 'New York',
    state: 'NY',
    zip: '10001',
    country: 'US'
  },
  to: {
    name: 'Jane Doe',
    address: '456 Market Street',
    city: 'San Francisco',
    state: 'CA',
    zip: '94103',
    country: 'US'
  },
  package: {
    weight: 2.5,
    dimensions: [10, 7, 4],
    units: 'IN'
  }
}

shipment = dhl.create_shipment(shipping_info)

puts "Shipment created with ID: #{shipment[:id]}"
```

## Development

After checking out the repo, run `bin/setup` to install dependencies. Then, run `rake spec` to run the tests. You can also run `bin/console` for an interactive prompt that will allow you to experiment.

To install this gem onto your local machine, run `bundle exec rake install`. To release a new version, update the version number in `version.rb`, and then run `bundle exec rake release`, which will create a git tag for the version, push git commits and the created tag, and push the `.gem` file to [rubygems.org](https://rubygems.org).

## Contributing

Bug reports and pull requests are welcome on GitHub at https://github.com/Sylvance/overseas. This project is intended to be a safe, welcoming space for collaboration, and contributors are expected to adhere to the [code of conduct](https://github.com/Sylvance/overseas/blob/main/CODE_OF_CONDUCT.md).

## Code of Conduct

Everyone interacting in the Overseas project's codebases, issue trackers, chat rooms and mailing lists is expected to follow the [code of conduct](https://github.com/Sylvance/overseas/blob/main/CODE_OF_CONDUCT.md).
