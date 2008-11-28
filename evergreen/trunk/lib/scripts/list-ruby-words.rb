#!/usr/bin/ruby -w

# Outputs an unsorted list of unique words found in class and method names
# via reflection. This is used to seed Evergreen's Ruby spelling exceptions.
# The assumption is that the Ruby interpreter that's called is the Ruby
# interpreter you're targeting, or is close enough that you don't care.

# This one we need...
require "set.rb"
# These ones we just drag in so we include them in our output...
require "pathname.rb"
require "thread.rb"

names = Set.new()
ObjectSpace.each_object(Class) {
    |c|
    # We don't worry about breaking SystemCallError (say) into words: the
    # calling code does that anyway.
    names.merge(c.to_s().split(/::/))
    c.methods().each() {
        |m|
        method_name = m.to_s()
        # Ignore operators like "<<" or "[]=".
        if method_name =~ /[A-Za-z]/
            # Tidy names like "zero?", "sub!", and "critical=".
            method_name = method_name.gsub(/[?!=]$/, "")
            # Tidy leading and trailing underscores.
            method_name = method_name.gsub(/^_+/, "")
            method_name = method_name.gsub(/_+$/, "")
            names.merge(method_name.split(/_/))
        end
    }
}
# We don't bother sorting, because the calling code does that anyway.
puts(names.to_a())