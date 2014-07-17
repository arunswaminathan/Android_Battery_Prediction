Android_Battery_Prediction
==========================

This Android application is designed to run a set of workload tests, and show the current battery levels of the device.
It	runs	a	set	of	workload	tests	from	time	to	time	and	

generates	a	test	report. The	test	report	contains	the	details	regarding	the	current	battery	

status.	

The	idea behind	this	application	was	to	make	battery	prediction	accurate.	Instead	of	just	

reporting	the	current	battery	and	extrapolating	the	expected	time	until	its	runs	out,	this	

app	simulates	a	normal	usage	and	then	predicts	the	battery	based	on	the	simulated	

usage.

Introduction
------------

The	workload tests	are	designed	to	simulate a	general	usage	of	the	device	by	an	average	

user.	The	workload	test	includes	the	following	actions:

1. Launching	a	random	website	in	a	new	tab	of	an	existing	browser instance

2. Launching	Google	Maps	and	showing	the	current	location	on	the	map

3. Launching	and	running	the	execution	of	a	random	application	on	the	phone	using	

the	MonkeyRunner	api	for	Android.

These	workload	tests	are	performed	repeatedly	in	fixed	time	intervals.	The	battery	

consumption	is	reported	in	the	form	of	a	graph	which	plots	the	Output	Voltage	of	the	

battery.	

Tools	Used
------------

Eclipse	v3.8

Android	Development	Tools	v22.3.0

Emulator	Nexus-4	AVD

AndoidPlot	library	0.6.0

MonkeyRunner	API	

User	Interface
-----------------

The	user	interface	displays	all	the	following	data:

• Current	level	in	%

• Expected	duration	the	battery	will	last

• Current	battery	temperature in	C

• CPU	free	%

• Current	battery	output	current	in	mA

• No.	of		processor	cores	in	the	device

• WiFi	status

• Bluetooth	status

• Time	of	last	workload	test

It	also	displays	a	graph	of	the	battery	voltage	over	the	course	of	the	workload	tests.

Principle
---------

The	principle	behind	battery	prediction	is	to	divide	the	battery	capacity	in	mAh	by	the	

average	current	in	mA	over	the	course	of	the	workload to	produce	the	expected	time. But	

it	isn’t	possible	to	explicitly	know	the	current	delivered	by	the	battery.	After	several	

experiments	and	help	from	various	other	groups,	I	found	out	that	there	is	no	way	of	

getting	Battery	Current	value	through	software	only	(as	its	not	supported	in	h/w).	Only	

way	I	found	was	to	measure	current	flowing	through	battery	by	means	of	multimeter.

This	method	is	not	useful	for	this	purpose.	Instead,	I	use	the	principle	that	internal	

resistance	of	a	battery	is	constant,	specifically	for	Li-On	batteries,	which	power	all	mobile	

devices	today[1].	This	means	the	current	changes	proportional	to	the	voltage.	Therefore,	I	

use	the	voltage	to	predict	battery	life.	The	output	of	the	battery	during	the	course	of	the	

workload	is	stored	in	an	arraylist.	The	average	value	of	the	voltages	stored	in	the	

arraylist	is	used	in	the	computation.	The	capacity	of	the	battery	is	captured	by	obtaining	

the	manufacturer	and	model	of	the	device,	and	then	using	the	known	battery	capacities	

of	devices.

Program	Details
---------------

The	BatteryManager	class	

(http://developer.android.com/reference/android/os/BatteryManager.html)	provides	

the	various	functions	to	extract	data	regarding	the	battery.

The	following	extras	of	the	BatterManager	class	are	used

• EXTRA_VOLTAGE

• EXTRA_LEVEL

• EXTRA_SCALE

• EXTRA_TEMPERATURE

• EXTRA_VOLTAGE

• EXTRA_ICON_SMALL

Nested	timers	are	used	to	run	the	workload	tests.	An	outer	timer	triggers	every	45	

seconds.	The	inner	countdown	timer	counts	down	from	10	and	launches	a	new	tab	in	the	

browser	when	it	reaches	0.	The	link	opened	in	the	browser	alternates	between	a	link	

from	a	random	set	of	links	and	a	link	to	Google	Maps.

AndroidPlot	is	used	to	plot	the	required	graph	(http://androidplot.com/).	AndroidPlot	is	

an	API	for	creating	dynamic	and	static	charts	within	your	Android	application.	It	was	

designed	from	the	ground	up	for	the	Android	platform,	is	compatible	with	all	versions	of	

Android	from	1.6	onward and	is used by over 500 apps on	the	market	today.

The	information	from	the	EXTRA_VOLTAGE	is	stored	in	an	arraylist.	This	arraylist	is	then	

plotted	on	the	graph	using	the	AndroidPlot	library.

MonkeyRunner	API
-------------------

The	MonkeyRunner	tool	provides	an	API	for	writing	programs	that	control	an	Android	device	

or	emulator	from outside	of	Android	code.	With	MonkeyRunner,	you	can	write	a	Python	

program	that	installs	an	Android	application	or	test	package,	runs	it,	sends	keystrokes	to	it,	

takes	screenshots	of	its	user	interface,	and	stores	screenshots	on	the	workstation.

The	MonkeyRunner	script	I	have	written	performs	the	following	actions:

1. Clicks	the	Home	button

2. Clicks	the	Apps	button

3. Clicks	on	a	random	app	on	the	screen

4. Waits	for	5	seconds

5. Takes	a	screenshot

6. Clicks	on	the	Home	button

7. Clicks	on	the	Apps	button

8. Clicks	on	the	Battery	Prediction	app

The	MoneyRunner script	is	executed	on	the	terminal	using	the	Python	command.	The	script	

is	executed	on	the	Emulator	as	well	as	the	device	(if	connected).
