import uuid
import yaml
import re

from flask import Flask, flash, request, render_template, redirect, render_template
from decimal import *

app = Flask(__name__)
app.secret_key = 'test'
ORDER_DB = 'orders.yml'

with open('products.yml') as _f:
    PRODUCTS = yaml.safe_load(_f)

with open('denominations.yml') as _f:
    DENOMINATIONS = yaml.safe_load(_f)

#Adding the order details to the ORDER_DB file
def record_order(buyer,product_id,amount_paid):
    
    order_id = str(uuid.uuid4()).split('-', 1)[0]
    orders = {
        order_id: {
            'buyer': buyer,
            'product_id': product_id,
            'amount_paid': amount_paid,
        }
    }
    
    with open(ORDER_DB, 'a') as f:
        f.write(yaml.dump(orders, default_flow_style=False))

    return order_id


@app.route('/', methods=['POST', 'GET'])
def index():
    context = {}

    if request.method == 'POST':
        
        product_id = request.form.get('product')  #Changed poduct_id to product to read the input from the product field
        amount_paid = request.form.get('paid')
        buyer = request.form.get('buyer')

        amount_paid = re.sub(r"[^0-9.]","",amount_paid) #Removing non-number characters but allowing for decimal points

        amount_paid = Decimal(amount_paid) * 100 #Changing to penny value so we can compare change types later
        amount_paid = int(amount_paid)

        # User has not selected a fruit
        if not product_id:
            flash('Please enter a fruit')
        
        #Checking if user has paid enough for the product, notifying them if not
        elif(amount_paid < 1 or amount_paid < (PRODUCTS[int(product_id)]['price']*100)):
            tempOut = str(amount_paid/100) + " is not enough for the chosen product, the total cost is: " + str(PRODUCTS[int(product_id)]['price'])
            flash(tempOut)

        # User has selected a fruit, and paid the correct amount
        else:

            order_id = record_order(buyer,int(product_id),amount_paid)
            flash('Order Success!', 'success')

            return redirect('/confirmation/'+order_id+'/')
            
    return render_template('index.jinja', products=PRODUCTS, title='Order Form', **context)


@app.route('/confirmation/<order_id>/')
def confirmation(order_id):
    with open(ORDER_DB) as f:
        orders = yaml.safe_load(f) or {}

    with open('denominations.yml') as _f:
        DENOMINATIONS = yaml.safe_load(_f)

    order = orders.get(order_id)
    if order is None:
        pass
    else:
        item_price = PRODUCTS[order['product_id']]['price']
        amount_paid = order['amount_paid']
        change_due = int(amount_paid) - item_price*100

        money = ()
        tempChange = change_due

        #Iterating through all the demoninations to count the return change
        for denom in DENOMINATIONS:
            num = tempChange/denom['value']
            money += (re.sub(r"[^0-9£p]","",denom['name']),) * int(num) #Adding the denominations to the tuple money, and using a regex to remove unwanted characters
            tempChange -= denom['value'] * int(num)   

        change_due = float(change_due)/100
        amount_paid = float(amount_paid)/100

    return render_template('confirmation.jinja', order_id=order_id, amount_paid=amount_paid, item_price=item_price, change_due=change_due, denoms=money, title='Order Confirmation')

@app.route('/orderlist')
def orderList():
    
    with open(ORDER_DB) as f:
        orders = yaml.safe_load(f) or {}    

    return render_template('orderList.jinja', order_list=orders, title='Order List')

if __name__ == '__main__':
    app.run(debug=True, use_reloader=True)
