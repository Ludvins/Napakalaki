# To change this license header, choose License Headers in Project Properties.
# To change this template file, choose Tools | Templates
# and open the template in the editor.

module NapakalakiGame

require "singleton"

class Dice
  
  include Singleton
    
  def nextNumber
    1 + rand(6)
  end
end
end