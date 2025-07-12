## 1. Principi guida

- **Separation of Concerns**: ogni componente ha una sola responsabilità.  
- **DDD-style**:  
  - **Aggregate root** (`Player`) espone metodi di dominio per mutare il proprio stato.  
  - **Domain services** (`DeckService`, `ComboService`) eseguono logica che non appartiene a un singolo aggregate.  
  - **Application service** (`CardManager`) orchestra il flusso di gioco.  
- **Pattern principali**:  
  - **Factory** per la creazione di carte obiettivo;  
  - **Strategy** (builder e `ComboRule`) per le regole variabili;  
  - **Template Method** per la logica comune di costruzione carta;  
  - **Layered Architecture** (Domain, Application, Infrastructure).

---

## 2. Modello di dominio

1. **Carte** (`model.cards`)  
   - `Card` (interfaccia)  
   - `TerritoryCard` (tipo: Jack/Cavalry/Artillery + territorio)  
   - `WildCard` (jolly)  

2. **Mano** (`model.player.Hand`)  
   - Contiene esclusivamente una collezione di `Card`.  
   - Metodi:  
     - `addCard(Card c)`  
     - `getCards(): List<Card>`  
     - `setCards(Collection<Card>)`

3. **Combo** (`model.combo`)  
   - `Combo` (record): lista di 3 carte + numero di truppe assegnate.  
   - `ComboRule` (interfaccia): `matches(List<Card>)` + `getTroopAward()`.  
   - Implementazioni tipiche:  
     - `ThreeOfKindRule` (tre dello stesso tipo + wild)  
     - `OneOfEachRule` (uno di ogni tipo + wild)  

4. **Contesto di valutazione**  
   - `ComboEvaluator`: genera tutte le triple di carte in mano e verifica, per ciascuna, tutte le `ComboRule`, restituendo le combo valide.

---

## 3. Servizi di dominio

1. **DeckService** (`model.deck.DeckService`)  
   - Tiene internamente `deck` e `discardPile`.  
   - Metodi:  
     - `draw(): Card` (pesca e, se necessario, rifà reshuffle)  
     - `discard(List<Card>)`

2. **ComboService** (`model.combo.ComboService`)  
   - Espone `findCombos(List<Card>): List<Combo>`  
   - Incapsula `ComboEvaluator` e la lista delle regole registrate.

---

## 4. Aggregate root e metodi di dominio

- **Player** (`model.player.Player`)  
  - Campi: `Hand hand`, scorte di truppe, territori, ecc.  
  - Metodi di alto livello:  
    - `drawCard()`: invoca internamente `DeckService.draw()` e `hand.addCard(...)`.  
    - `findCombos(): List<Combo>`: delega a `ComboService`.  
    - `cashInCombo(Combo)`:  
      ```java
      // Rimuove le 3 carte da hand
      hand.removeAll(combo.cards());
      // Discard delle carte usate
      deckService.discard(combo.cards());
      // Aggiunge le truppe al giocatore
      addTroops(combo.troopAward());
      ```

---

## 5. Application Service / Controller

- **CardManager** (`model.controller.CardManager`)  
  - Orchestration layer che fornisce l’API per la UI o per il turno di gioco:  
    1. `dealCardTo(Player)`: chiama `player.drawCard()`  
    2. `listCombosOf(Player)`: chiama `player.findCombos()`  
    3. `useCombo(Player, Combo)`: chiama `player.cashInCombo(combo)`  

---

## 6. Vantaggi dell’architettura

- **Incapsulamento**: lo stato del giocatore (mano, truppe) è modificato solo da metodi di `Player`.  
- **Modularità**: aggiungere nuove regole di combo o tipi di carta non richiede di toccare `Player` né `CardManager`, ma solo di registrare nuove `ComboRule` o creare nuove classi di `Card`.  
- **Testabilità**:  
  - Tutte le componenti (builder, service, evaluator) si possono testare in isolamento usando mock delle dipendenze.  
  - I metodi di `Player` e `CardManager` si testano con `assertThrows`, `assertTrue/assertFalse`, verificando scenari controllati.  
- **Estendibilità**: segue il principio Open/Closed: nuove funzionalità si aggiungono tramite estensione, non tramite modifica di codice esistente.

---

## 7. Controllo sulla validità della cata territorio
- **Contorollo**: Il contorllo relativo nella creazione della carta territorio esita e si passi un territorio valido
viene fatto direttamnte dal metodo `createDeck`

**Conclusione**  
Questa architettura, ispirata a DDD e ai classici pattern di design, garantisce un codice **pulito**, **facilmente manutenibile** e **scalabile** per la gestione delle carte-territorio, del mazzo e delle combo in Risiko.  
