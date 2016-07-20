#------------------------------------------------------------------
# FOLLOW THE BELLOW EXAMPLE TO CREATE YOUR PAGE OBJECT CLASSES
#------------------------------------------------------------------
require_relative '../../../features/support/pages/common/common_UI_Functions'
class Signup < MobTest::Base

  # IOS elements and common methods
  ios do

    button(:join, xpath: '//UIAApplication[1]/UIAWindow[1]/UIAButton[1]')
    text_field(:text1, xpath: '//UIATextField[1]')
    text_field(:text2, xpath: '//UIATextField[2]')
    text_field(:qutext, xpath:'//UIAStaticText[contains(@name, "<Privacy quote>")]')
    button(:next, xpath: '//UIAButton[@name="NEXT"]')
    text_field(:mobtext, xpath:'//UIAStaticText[contains(@name, "We use your mobile number")]')
    button(:alertOK, xpath: '//UIAAlert[1]/UIACollectionView[1]/UIACollectionCell[1]/UIAButton[1]')
    element(:create_PIN1, name: '1', class: 'UIAKey')
    element(:create_PIN2, name: '2', class: 'UIAKey')
    element(:create_PIN3, name: '3', class: 'UIAKey')
    element(:create_PIN4, name: '4', class: 'UIAKey')



    def clickOK
      alertOK_element.click()
    end

    def enterPin
      create_PIN1_element.click
      create_PIN2_element.click
      create_PIN3_element.click
      create_PIN4_element.click
    end

  end

  # Android elements and methods
  android do
    # label(:welcome, xpath: '//android.widget.TextView[contains(@text,"Transparantie")]')
    label(:welcome, id: "#{$android_package}:id/viewPagerCountDots")
    button(:join, id: "#{$android_package}:id/join")
    text_field(:fname, id: "#{$android_package}:id/first_name")
    text_field(:lname, id: "#{$android_package}:id/last_name")
    button(:next, id: "#{$android_package}:id/next")
    button(:previous, id: "#{$android_package}:id/previous")
    text_field(:mnumber, id: "#{$android_package}:id/mobile_number")
    text_field(:nickname, id: "#{$android_package}:id/nick_name")
    text_field(:passcode, id: "#{$android_package}:id/passcode1")
    # button(:nextwlk, xpath: '//android.widget.ImageButton[1]')
    button(:nextwlk, id: "#{$android_package}:id/btn_next")
    element(:chlgs_title, xpath: '//android.widget.TextView[@text="CHALLENGES"]')
    button(:permOK,id:"android:id/button1")
    button(:btnswitch,id:"android:id/switchWidget")



    def checkWelcomeScreen
      displayed=false
      begin
        # puts "Element=#{welcome_element.displayed?}"
        if (welcome_element.displayed?)

          while not(displayed)
            begin
              # puts "Displayed=#{displayed}"
              nextwlk_element.click
              displayed=join_element.displayed?
            rescue => e
              displayed=false
            end
          end
        end
      rescue Exception => e
        puts "Exception message #{e.message}"
        puts "No welcome screen"
      end
    end

    def enterName(text1, text2)
      # puts "Environment=#{ENV['PLATFORM']}"
      sleep 1
      checkKeyBoard
      fname_element.send_keys text1;
      checkKeyBoard
      lname_element.send_keys text2;
      checkKeyBoard
    end

    def enterMobile(text1, text2)
      # puts "Environment=#{ENV['PLATFORM']}"
      sleep 1
      checkKeyBoard
      mnumber_element.send_keys text1;
      checkKeyBoard
      nickname_element.send_keys text2;
      checkKeyBoard
    end

    def clickOK
      puts "No popup, we are in Android"
    end

    def enterPin
      # passcode_element.click
      passcode_element.send_keys '1234'

    end

    def landed_on_challenges?
      bool = false
      sleep 3
      begin
        puts "Displayed=#{chlgs_title_element.displayed?}"
        bool=self.chlgs_title_element.displayed?

      rescue Exception => e
        puts "Message=#{e.message}"
        return false
      end
      bool
    end



    # Check for Android Native Keybaord and dissmiss it if present
    def checkKeyBoard
      if (ENV['PLATFORM'] == 'android')
        if(COMMON_UI.checkUIKeyboard)
          @driver.navigate.back
        end
      end
    end

    def singnuptoYona
      checkWelcomeScreen
      sleep 2
      join_element.click()
      sleep 1
      enterName('fname','lname')
      next_element.click();
      begin
        strMob=rand(1000000000).to_s
      end while strMob.length!=9
      enterMobile(strMob,'Nicky')
      next_element.click();
      sleep 2
      enterPin
      sleep 2
      enterPin
      sleep 2
      enterPin
      sleep 2
      # checkPermissionPopup

    end

    def checkPermissionPopup
      begin
        sleep 2
        webel=@driver.find_elements(:id,'android:id/button1')
        puts "empty=#{webel.size}"
        if(webel.size>0)
          permOK_element.click
          sleep 1
          btnswitch_element.click
          sleep 1
          permOK_element.click
          @driver.navigate.back
          sleep 1
          enterPin
        end
      rescue Excpetion => e
        puts "Excepiton occurred"
        puts e.message
      end
    end


  end



  def next(scrName)

    if (scrName=='namescreen' ) && (ENV['PLATFORM'] == 'ios')
      qutext_element.click();
    end
  else if (scrName=='mobnumscreen') && (ENV['PLATFORM'] == 'ios')
         mobtext_element.click();
       end
  next_element.click();
  end






end
