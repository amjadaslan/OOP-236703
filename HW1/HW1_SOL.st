Object subclass: #Monom
	instanceVariableNames: 'exp coef'
	classVariableNames: ''
	poolDictionaries: ''
	category: 'OOP1'!

!Monom methodsFor: 'coef' stamp: '0 0 11/7/2021 15:47'!
coef
	^coef! !

!Monom methodsFor: 'coef' stamp: '0 0 11/7/2021 15:49'!
coef: anInteger
	(anInteger isInteger)
	ifTrue: [coef := anInteger]
	ifFalse: [self error: 'invalid input']! !


!Monom methodsFor: 'exp' stamp: '0 0 11/7/2021 15:49'!
exp
	^exp! !

!Monom methodsFor: 'exp' stamp: '0 0 11/7/2021 15:52'!
exp: anInteger
	((anInteger isInteger) and: [anInteger>=0])
	ifTrue: [exp := anInteger]
	ifFalse: [self error: 'invalid input']! !


!Monom methodsFor: 'Derivative' stamp: '0 0 11/7/2021 15:52'!
Derivative |newMonom|
	newMonom := Monom new.
	
	((self exp)=0) 
		ifFalse: [newMonom coef: (self coef)*(self exp); exp: (self exp)-1. ^newMonom]
		ifTrue: [newMonom initialize. ^newMonom]! !


!Monom methodsFor: 'Integrate' stamp: '0 0 11/7/2021 15:52'!
Integrate |newMonom|
	newMonom := Monom new.
	newMonom coef: coef/(exp+1); exp: exp+1.
	^newMonom! !


!Monom methodsFor: 'initialize-release' stamp: '0 0 11/7/2021 15:47'!
initialize
 exp :=0.
 coef :=0.! !


Object subclass: #PolyStream
	instanceVariableNames: 'block'
	classVariableNames: ''
	poolDictionaries: ''
	category: 'OOP1'!

!PolyStream methodsFor: 'block' stamp: '0 0 11/8/2021 10:45'!
block
	^block! !


!PolyStream methodsFor: 'addCoef: withExp:' stamp: '0 0 11/8/2021 16:10'!
addCoef: coef withExp: exp
	
	(((exp isInteger) and: (coef isInteger)) and: (exp >= 0))
	ifFalse: [self error: 'invalid input']
	ifTrue: [|temp|
		temp:=block.
	   block:=[:x :expToRemove| 
			(expToRemove includesKey: exp)
				ifTrue:[
						temp value:x value:expToRemove.
					   ]
				ifFalse:[
						(coef*(x raisedToInteger: exp))+(temp value:x value:expToRemove).
						].
				
									
				]
		
			]! !


!PolyStream methodsFor: 'add' stamp: '0 0 11/8/2021 15:19'!
add: aPolyStream
	(aPolyStream isKindOf: PolyStream)
		ifFalse: [self error: 'invalid input']
		ifTrue: [|temp|
			temp:=block.
			block:=[:x :expToRemove |
				((temp value:x value:expToRemove)+((aPolyStream block) value:x value:expToRemove))
				].			
			]! !


!PolyStream methodsFor: 'multiplyBy' stamp: '0 0 11/8/2021 15:40'!
multiplyBy: anInteger
	(anInteger isInteger)
	ifFalse:[self error: 'invalid input']
	ifTrue:[
			|temp|
			temp:=block.
			block:=[:x :expToRemove |
				anInteger*(temp value:x value:expToRemove)
				]
			
		]! !


!PolyStream methodsFor: 'filter' stamp: '0 0 11/8/2021 16:17'!
filter: aSet

	|copiedSet temp|
	copiedSet:=aSet deepCopy.
	temp:=block.
	block:=[:x :expToRemove |
		copiedSet do:[:a | expToRemove add: a->0.].
		
		temp value:x value:expToRemove.
		]! !


!PolyStream methodsFor: 'eval' stamp: '0 0 11/8/2021 14:07'!
eval: anInteger
	(anInteger isInteger)
	ifFalse: [self error: 'invalid input']
	ifTrue: [
		|tot|
		tot:=self block value: anInteger value: (Dictionary new).
		^tot
		]! !


!PolyStream methodsFor: 'substitute' stamp: '0 0 11/8/2021 15:36'!
substitute: anInteger
	((anInteger isInteger) and: [(anInteger~=0)])
	ifFalse:[self error: 'invalid input']
	ifTrue:[
		|temp|
			temp:=block.
			block:=[:x :expToRemove | 
					temp value: (anInteger * x) value: expToRemove.
					]
	]! !


!PolyStream methodsFor: 'initialize-release' stamp: '0 0 11/8/2021 16:06'!
initialize
	block := [:x :expToRemove| 
			expToRemove:= Dictionary new.
			0].! !


Object subclass: #Polynom
	instanceVariableNames: 'monoms'
	classVariableNames: ''
	poolDictionaries: ''
	category: 'OOP1'!

!Polynom methodsFor: 'add' stamp: '0 0 11/8/2021 15:54'!
add: aPolynom |newPolynom|
	newPolynom := Polynom new.
	newPolynom initialize.
	(aPolynom isKindOf: Polynom)
	ifFalse:[self error: 'invalid input']
	ifTrue: [
		
		monoms do: [:a| 
				newPolynom addMonom: a.
			].
		aPolynom monoms do: [:a| 
				newPolynom addMonom: a.
			].
		^newPolynom
		].
	! !

!Polynom methodsFor: 'add' stamp: '0 0 11/8/2021 15:53'!
addMonom: aMonom |flag|
	(aMonom isKindOf: Monom)
	ifFalse: [self error: 'invalid input']
	ifTrue: 
		[
		(aMonom coef = 0) ifFalse:[
		flag:=0.
		monoms do: 
			[:a| 
			(a exp = aMonom exp)
			ifTrue: [
					a coef: (a coef + aMonom coef).
					flag := 1.
					(a coef = 0)
						ifTrue: [monoms remove: a ]
					]
			].
		
			(flag = 0)
			ifTrue: [monoms add: aMonom copy ].
	
		]]! !


!Polynom methodsFor: 'Mul' stamp: '0 0 11/7/2021 20:07'!
multiplyByMonom: aMonom
	(aMonom isKindOf: Monom)
	ifFalse: [self error: 'invalid input']
	ifTrue: 
		[
		(aMonom coef = 0)
		ifTrue: [
			monoms removeAll.
			monoms initialize]
		ifFalse:[	
		monoms do: [:a |
			a coef: (aMonom coef)*(a coef). 
			a exp: (aMonom exp)+(a exp).
			]
		]]! !


!Polynom methodsFor: 'asDictionary' stamp: '0 0 11/14/2021 19:07'!
asDictionary |dict|
	dict := Dictionary new.
		monoms do: [:a| 
				(a coef = 0)
				ifFalse:[
				dict add: a exp -> a coef]
						].
				^dict


		
		
		! !


!Polynom methodsFor: 'Integrate' stamp: '0 0 11/7/2021 17:15'!
Integrate |newPolynom|
	newPolynom := Polynom new.
	newPolynom initialize.
	monoms do: [:a|
		newPolynom addMonom: (a Integrate).
		].
	^newPolynom! !


!Polynom methodsFor: 'Derivative' stamp: '0 0 11/7/2021 20:00'!
Derivative |newPolynom|
	newPolynom := Polynom new.
	newPolynom initialize.
	monoms do: [:a|
		 (a Derivative coef = 0)
		ifFalse: [
			newPolynom addMonom: (a Derivative).
			]
		].
	^newPolynom
	! !


!Polynom methodsFor: 'eval' stamp: '0 0 11/7/2021 17:23'!
eval: anInteger |tot|
	tot := 0.
	(anInteger isInteger)
	ifFalse: [self error: 'invalid input']
	ifTrue: [
		monoms do: [:a|
			tot:= tot + ((a coef)*(anInteger raisedTo:(a exp))).
			].
		^tot
		]
	! !


!Polynom methodsFor: 'monoms' stamp: '0 0 11/7/2021 19:20'!
monoms
	^monoms! !


!Polynom methodsFor: 'initialize-release' stamp: '0 0 11/7/2021 15:57'!
initialize
	|mon|
	monoms := OrderedCollection new.
	mon := Monom new.
	mon initialize.
	monoms add: mon.! !
