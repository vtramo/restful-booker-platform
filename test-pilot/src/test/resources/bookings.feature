Feature: Making a booking for a room

  Scenario: User makes a booking
    Given There is a free room
    And I am on the homepage
    When I make a booking
    Then I will see "Booking Successful" on the screen
    Then I will see "Unavailable" on newly booked calendar days