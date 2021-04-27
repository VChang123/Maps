<#assign content>

  <div class="top">
    <div class="instructions">
      <h1> stars search :~) </h1>
      <p>want to learn more about a star?
        input its name or its coordinates and you can search for its nearest
        neighbors or the stars around it within a certain radius!
      </p>
      <p>
        as an example, i can put in 5 max neighbors and either "Sol" in star
        name or 0 0 0 in coordinates.
        <em>if searching for a star by name, please input the name in quotes.</em>
      </p>
    </div>

    <div class="star-img">
      <img src="assets/star.gif" alt="pink, blue, and yellow twinkling stars">
    </div>
  </div>

  <div class="content-container">
    <div class="bubble">
      <form method="GET" action="/neighbors-KD">
        <p> <strong>neighbors</strong> </p>
        <label for="num">max neighbors</label><br>
        <textarea name="num"></textarea><br>
        <div class="star-name">
          <label for="star-name">name of star </label><br>
          <textarea name="star-name"></textarea><br>
        </div>
        <p class="or"> OR </p>
        <p class="coordinates-label"> coordinates </p>
        <div class="coord-container">
          <div class="coordinate">
            <label for="x">x</label><br>
            <textarea name="x" class="coord-input"></textarea><br>
          </div>
          <div class="coordinate">
            <label for="y">y</label><br>
            <textarea name="y" class="coord-input"></textarea><br>
          </div>
          <div class="coordinate">
            <label for="z">z</label><br>
            <textarea name="z" class="coord-input"></textarea><br>
          </div>
        </div>
        <input type="submit">
      </form>
    </div>

    <div class="bubble">
      <form method="GET" action="/radius-KD">
        <p> <strong>radius</strong> </p>
        <label for="num">max radius</label><br>
        <textarea name="num"></textarea><br>
        <div class="star-name">
          <label for="star-name">name of star </label><br>
          <textarea name="star-name"></textarea><br>
        </div>
        <p class="or"> OR </p>
        <p class="coordinates-label"> coordinates </p>
        <div class="coord-container">
          <div class="coordinate">
            <label for="x">x</label><br>
            <textarea name="x" class="coord-input"></textarea><br>
          </div>
          <div class="coordinate">
            <label for="y">y</label><br>
            <textarea name="y" class="coord-input"></textarea><br>
          </div>
          <div class="coordinate">
            <label for="z">z</label><br>
            <textarea name="z" class="coord-input"></textarea><br>
          </div>
        </div>
        <input type="submit">
      </form>
    </div>

    <div class="bubble">
      <form method="GET" action="/neighbors-naive">
        <p> <strong>naive neighbors</strong> </p>
        <label for="num">max neighbors</label><br>
        <textarea name="num"></textarea><br>
        <div class="star-name">
          <label for="star-name">name of star </label><br>
          <textarea name="star-name"></textarea><br>
        </div>
        <p class="or"> OR </p>
        <p class="coordinates-label"> coordinates </p>
        <div class="coord-container">
          <div class="coordinate">
            <label for="x">x</label><br>
            <textarea name="x" class="coord-input"></textarea><br>
          </div>
          <div class="coordinate">
            <label for="y">y</label><br>
            <textarea name="y" class="coord-input"></textarea><br>
          </div>
          <div class="coordinate">
            <label for="z">z</label><br>
            <textarea name="z" class="coord-input"></textarea><br>
          </div>
        </div>
        <input type="submit">
      </form>
    </div>

    <div class="bubble">
      <form method="GET" action="/radius-naive">
        <p> <strong>naive radius</strong> </p>
        <label for="num">max radius</label><br>
        <textarea name="num"></textarea><br>
        <div class="star-name">
          <label for="star-name">name of star </label><br>
          <textarea name="star-name"></textarea><br>
        </div>
        <p class="or"> OR </p>
        <p class="coordinates-label"> coordinates </p>
        <div class="coord-container">
          <div class="coordinate">
            <label for="x">x</label><br>
            <textarea name="x" class="coord-input"></textarea><br>
          </div>
          <div class="coordinate">
            <label for="y">y</label><br>
            <textarea name="y" class="coord-input"></textarea><br>
          </div>
          <div class="coordinate">
            <label for="z">z</label><br>
            <textarea name="z" class="coord-input"></textarea><br>
          </div>
        </div>
        <input type="submit">
      </form>
    </div>

    <div class="results">
      <p> <strong>results:</strong> </p>
      ${results}
    </div>

  </div>

</#assign>
<#include "main.ftl">