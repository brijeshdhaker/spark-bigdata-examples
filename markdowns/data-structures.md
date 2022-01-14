### Choosing a Data structure for very large data
The choice of structure depends heavily on how much memory you have available. I'm assuming based on the description that you need lookup but not to loop over them, find nearest, or other similar operations.

Best is probably a bucketed hash table. By placing hash collisions into buckets and keeping separate arrays in the bucket for keys and values, you can both reduce the size of the table proper and take advantage of CPU cache speedup when searching a bucket. Linear search within a bucket may even end up faster than binary search!

AVL trees are nice for data sets that are read-intensive but not read-only AND require ordered enumeration, find nearest and similar operations, but they're an annoyingly amount of work to implement correctly. You may get better performance with a B-tree because of CPU cache behavior, though, especially a cache-oblivious B-tree algorithm.
### Efficient way to search large data set based on several fields
For example, Person object is defined as following:

Person:
first name
last name
phone numbers
I have 100k objects of Person type and I want to search for a particular person based on any of the field ?


There is no one answer to this, because the right answer depends (heavily) upon how much you care about speed vs. extra storage.

If you want absolute maximum speed, and don't care at all about using extra storage, yes, you can create three copies of the data, one sorted by each field, and when a search is entered, just use the appropriate one. This may not be nearly as awful an option is it might first appear. Let's assume your strings average about 10 bytes apiece, so the overall size of the struct is ~30 bytes. 100'000 of those gives roughly 3 megabytes per copy, for a grand total of about 9 megabytes. At one time that would have been clearly prohibitive -- but with a typical machine now having at least 8 gigabytes of RAM, it's not nearly so terrible.

Assuming you rule that out, the next most obvious possibility would be to build indexes into the raw data -- put the raw data into one array, then build one index for each field, where each entry in the index contains the data for one field, and a pointer/subscript to the main data. Each index entry can be ~14 bytes, so each index is about half the size of the overall data. With only three fields you don't save a lot, but you do save some -- and at an extremely minimal cost in complexity. With more fields, you'd save even more.

Another possibility would be to implement your indices as hash tables. The primary advantage here is that you can avoid storing date repeatedly. For example, if you compute a 16-bit hash with 2 entries per bucket, you can store one index in ~512K bytes. If a bucket is full, but neither entry matches your input, you re-hash and try another bucket. Keep going until you either find your item or find an empty bucket.