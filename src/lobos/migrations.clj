(ns lobos.migrations
  (:refer-clojure :exclude [alter drop
                            bigint boolean char double float time])
  (:use (lobos [migration :only [defmigration]] core schema
               config helpers)))

(defmigration add-gloats-table
  (up [] (create
          (tbl :gloats
               (integer :gloater_forecast_id)
               (integer :poor_slob_forecast_id)
               (text :message))))
  (down [] (drop (table :gloats))))

(defmigration add-forecasts-table
  (up [] (create
          (tbl :forecasts
               (varchar :location 255)
               (varchar :query 255)
               
               (varchar :current_weather_code 20)
               (float :current_temp_c)
               (float :current_humidity)
               
               (varchar :weather_code_1 20)
               (integer :temp_max_c_1)
               (integer :temp_min_c_1)
               (timestamp :date_1)
               
               (varchar :weather_code_2 20)
               (integer :temp_max_c_2)
               (integer :temp_min_c_2)
               (timestamp :date_2)

               (varchar :weather_code_3 20)
               (integer :temp_max_c_3)
               (integer :temp_min_c_3)
               (timestamp :date_3)
               
               (varchar :weather_code_4 20)
               (integer :temp_max_c_4)
               (integer :temp_min_c_4)
               (timestamp :date_4)

               (varchar :weather_code_5 20)
               (integer :temp_max_c_5)
               (integer :temp_min_c_5)
               (timestamp :date_5)))
      
      (create (index :forecasts [:location])))
  (down [] (drop (table :forecasts))))

(defn up
  []
  (migrate))