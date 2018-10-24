var cardFace = ["A","K","Q","J","10","9","8","7","6","5","4","3","2"];
var cardSuit = ["Diamonds","Hearts","Clubs","Spades"];
var cardDeck = [];
window.onload = load();

function getDeck()
{
	for (i in cardSuit)
	{
		for (ii in cardFace)
		{
			var card = new Object();
			card.value = cardFace[ii];
			card.suit = cardSuit[i];
			cardDeck.push(card);
		}
	}
	return cardDeck;
}

function shuffle()
{
	for (i=0; i<500; i+=1)
	{
		var rand1 = Math.floor(Math.random()*cardDeck.length);
		var rand2 = Math.floor(Math.random()*cardDeck.length);
		var tmpVal = cardDeck[rand1].value;
		var tmpSuit = cardDeck[rand1].suit;
		cardDeck[rand1].value = cardDeck[rand2].value;
		cardDeck[rand1].suit = cardDeck[rand2].suit;
		cardDeck[rand2].value = tmpVal;
		cardDeck[rand2].suit = tmpSuit;
	}
}

function renderDeck()
{
	for (i=0; i<cardDeck.length; i+=1)
	{
		var card = document.createElement("div");
		var value = document.createElement("div");
		var suit = getCardUI(cardDeck[i]);
		card.className = "card";
		// To give a complete feel, I'm also going to append the suit to the class so that I can change the color of text based on suit
		value.className = "value "+cardDeck[i].suit;
		card.appendChild(value);
		// Slight tweak here... Instructions say to append suit to variable "card" but then that wouldn't make sense in regards to the CSS if we want to affect value class as a child of card class
		value.appendChild(suit);
		document.getElementById("deck").appendChild(card);
	}
}

function getCardUI(card)
{
	// Slight tweak here... Instead of if/else statements, I prefer just a switch statement
	var v = document.createElement("div");
	var icon = "";
	switch (card.suit)
	{
		case "Hearts":
		  icon = "&hearts;";
		  break;
		case "Spades":
		  icon = "&spades;";
		  break;
		case "Clubs":
		  icon = "&clubs;";
		  break;
		case "Diamonds":
		  icon = "&#x25C6";
		  break;
	}
	v.innerHTML = card.value+"<br />"+icon;
	return v;
}

function load()
{
	cardDeck = getDeck();
	shuffle();
	renderDeck();
}
