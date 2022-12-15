#include "AVL.hpp"
#include <fstream>
#include <iostream>
#include <stack>
using namespace std;

//inner class Node constructor
AVL::Node::Node(const string& str, Node *parent, Node *left, Node *right){
    element = str;
    this->parent = parent;
    this->left = left;
    this->right = right;
}
//This method returns the parent of the caller node
AVL::Node* AVL::Node::getParent() const{
    return(parent);
}
//This method returns the left child of the caller node
AVL::Node* AVL::Node:: getLeft() const{
    return(left);
}
//This method returns the right child of the caller node
AVL::Node* AVL::Node::getRight() const{
    return(right);
}
//This method returns the content of the caller node
string AVL::Node::getElement() const{
    return(element);
}

//This method returns the height of the caller node
int AVL::Node::getHeight() const{
    return(height);
}
//This method sets the left child of the caller node
void AVL::Node::setLeft(Node* node){
    left = node;
}
//This method sets the right child of the caller node
void AVL::Node::setRight(Node* node){
    right = node;
}
//This method sets the parent of the caller node
void AVL::Node::setParent(Node* node){
    parent = node;
}
//This method sets the content of the caller node
void AVL::Node::setElement(string str){
    element = str;
}
//This method is checking if the caller node is left child
bool AVL::Node::isLeft() const{
    //if caller node is root
    if(this->getParent() == NULL){
        return(false);
    }

    else{
        return(this == this->getParent()->getLeft());
    }
}
//This method is checking if the caller node is right child
bool AVL::Node::isRight() const{
    //if caller node is root
    if(this->getParent() == NULL){
        return(false);
    }

    else{
        return(this == this->getParent()->getRight());
    }
}
//This method returns the height of the caller nodes right child
int AVL::Node::rightChildHeight() const{
    return(this->getRight() != NULL ? this->getRight()->getHeight() : 0);
}
//This method returns the height of the caller nodes left child
int AVL::Node::leftChildHeight() const{
    return(this->getLeft() != NULL ? this->getLeft()->getHeight() : 0);
}
//This method updates the height of the caller node, it returns 1 
//if the update was successful 
int AVL::Node::updateHeight(){
    //the new height is determined by the highest child of the caller node
    this->height = max(this->leftChildHeight(), this->rightChildHeight()) + 1;
    return(1);
}
//This method checks whether the subtree (with root the caller node) is balanced
bool AVL::Node::isBalanced() {
    int balance = this->leftChildHeight() - this->rightChildHeight();
    return((-1 <= balance) && (balance <= 1));
}
//This method is searching the node with the specified element
AVL::Node* AVL::findNode(string element, AVL::Node* currentNode){
    string nodeElement = currentNode->getElement();
    //if the given element is lexicographically smaller than the current node element,
    // check the left subtree
    if(element.compare(nodeElement) < 0){
        //if the current node is a leaf of the AVL tree
        if(currentNode->getLeft() == NULL){
            return(currentNode);
        }

        else
            return(findNode(element, currentNode->getLeft()));
    }
    //if the given element is lexicographically bigger than the current node element,
    // check the right subtree
    if(element.compare(nodeElement) > 0){
        //if the current node is a leaf of the AVL tree
        if(currentNode->getRight() == NULL){
            return(currentNode);
        }
        else
            return(findNode(element, currentNode->getRight()));
    }
    //current nodes content is equal to the given element
    return(currentNode);
}

//This method finds the highest child which is participating in the tree recontruction
AVL::Node* AVL::rebalanceSon(AVL::Node* node){
    if(node == NULL){
        return(NULL);
    }

    if(node->leftChildHeight() > node->rightChildHeight()){
        return(node->getLeft());
    }
    else if(node->leftChildHeight() < node->rightChildHeight()){
        return(node->getRight());
    }
    //both left and right child of the caller node have equal height, return the proper child based
    // on the caller nodes type of child
    else if(node->isLeft()){
        return(node->getLeft());
    }
    else
        return(node->getRight());
} 


AVL::Node* AVL::reconstruct(AVL::Node* node, AVL::Node* w, AVL::Node* u){
    //single-right rotation
    if(w->isLeft() && u->isLeft()){
        if(!(node == root)){
            if(node->isLeft())
                node->getParent()->setLeft(w);
            else
                node->getParent()->setRight(w);
            w->setParent(node->getParent());
        }

        node->setLeft(w->getRight());

        if(w->getRight() != NULL)
            w->getRight()->setParent(node);

        w->setRight(node);
        node->setParent(w);

        if(node == root){
            root = w;
            w->setParent(NULL);
        }
        return w;
    }
    //single-left rotation 
    else if(w->isRight() && u->isRight()){
        if(!(node == root)){
            if(node->isRight())
                node->getParent()->setRight(w);
            else
                node->getParent()->setLeft(w);
            w->setParent(node->getParent());
        }

        node->setRight(w->getLeft());

        if(w->getLeft() != NULL)
            w->getLeft()->setParent(node);

        w->setLeft(node);
        node->setParent(w);

        if(node == root){
            root = w;
            w->setParent(NULL);
        }
        return w;
    }
    //double-left rotation
    else if(u->isLeft()){
        node->setRight(u->getLeft());

        if(u->getLeft() != NULL)
            u->getLeft()->setParent(node);
        
        w->setLeft(u->getRight());
        
        if(u->getRight() != NULL)
            u->getRight()->setParent(w);
        if(!(node == root)){
            
            if(node->isRight())
                node->getParent()->setRight(u);
            else
                node->getParent()->setLeft(u);
            
            u->setParent(node->getParent());
        }
        node->setParent(u);
        w->setParent(u);
        u->setLeft(node);
        u->setRight(w);

        if(node == root){
            root = u;
            u->setParent(NULL);
        }
        return(u);
    }
    //double-right rotation
    else{
        node->setLeft(u->getRight());

        if(u->getRight() != NULL)
            u->getRight()->setParent(node);
        
        w->setRight(u->getLeft());
        
        if(u->getLeft() != NULL)
            u->getLeft()->setParent(w);
        if(!(node == root)){
            
            if(node->isLeft())
                node->getParent()->setLeft(u);
            else
                node->getParent()->setRight(u);
            
            u->setParent(node->getParent());
        }
        node->setParent(u);
        w->setParent(u);
        u->setRight(node);
        u->setLeft(w);

        if(node == root){
            root = u;
            u->setParent(NULL);
        }
        return(u);
    }
}
//This method is rebalancing the AVL tree, from the bottom to the top,
// starting from the subtree that's not balanced
void AVL::rebalance(AVL::Node* node){
    AVL::Node* u;
    AVL::Node* w;

    if(node == NULL)
        return;
    //while current node isn't the root of the tree
    while(node != NULL){
        //update the height of the current node
        node->updateHeight();
        //if the current node isn't balanced, both his son and grandson 
        //will participate in the reconstruction
            if(!node->isBalanced()){
            w = rebalanceSon(node);
            u = rebalanceSon(w);
            //reconstruct
            node = reconstruct(node,w,u);
            //fix the height of the nodes involved
            node->getLeft()->updateHeight();
            node->getRight()->updateHeight();
            node->updateHeight();
        }
        //set as new current node the current nodes parent
        node = node->getParent();
    }
}
//This method checks whether the given element exists in the existing AVL tree.
bool AVL::contains(string str){
    AVL::Node* node = findNode(str,this->root);

    if(node->getElement() == str)
        return(true);

    else
        return(false);
}
//This method creates and inserts a new node with the given element,
// if it doesn't already exist, and the method returns true. If 
//the element exists in the existing AVL tree, the insertion 
//is canceled and the method returns false.
bool AVL::add(string str){
    //if tree is empty, set the new node root of the tree
    if(this->size == 0){
        AVL::Node* node;
        node = new AVL::Node(str,NULL,NULL,NULL);
        this->root = node;
        this->size = 1;
        return(true);
    }
    //check if the node with the given element exists
    AVL::Node* insNode = findNode(str, this->root);

    //the node already exits, cancel the insertion
    if(insNode->getElement() == str)
        return(false);
    
    else{
        //if the given element is lexicographically smaller than the current node element,
        // set new node left child of the node returned
        //by the findNode method
        if(str.compare(insNode->getElement()) < 0){
            insNode->setLeft(new Node(str,insNode,NULL,NULL));
            insNode->getLeft()->setElement(str);
            //rebalance the AVL tree
            rebalance(insNode->getLeft());
        }

        else{//else set new node left child of the node returned by the findNode method
            insNode->setRight(new Node(str,insNode,NULL,NULL));
            insNode->getRight()->setElement(str);
            //rebalance the AVL tree
            rebalance(insNode->getRight());
        }
        //update the number of elements in the AVL tree by 1
        size++;
        return(true);
    }

}
//This method deletes the given node
void AVL::deleteNode(AVL::Node* delNode){
    if(delNode->getLeft() != NULL && delNode->getRight() != NULL){
        return; 
    }

    if(delNode == root){
        AVL::Node* notNullSon = (delNode->getLeft() != NULL ? delNode->getLeft():
                                delNode->getRight());
        root = notNullSon; //set this node as root
     
        if(root != NULL)
            root->setParent(NULL); //new root has no father anymore
    }
    else{
        AVL::Node* parentOfNode = delNode->getParent();
        AVL::Node* notNullSon = (delNode->getLeft() != NULL ? delNode->getLeft():
                                delNode->getRight());
        if(delNode->isLeft())
            parentOfNode->setLeft(notNullSon); //if node to be deleted is left son
        else
            parentOfNode->setRight(notNullSon); //if node to be deleted is right son
        if(notNullSon != NULL)
            notNullSon->setParent(parentOfNode); //change parent
    }
    size--; //change size of AVL tree
    //set every field of the node NULL, and delete
    delNode->setLeft(NULL);
    delNode->setRight(NULL);
    delNode->setParent(NULL);
    delete(delNode);
}
//This method is swapping the given nodes contents
void AVL::exchange(AVL::Node *v, AVL::Node *w){
    string temp = v->getElement();
    v->setElement(w->getElement());
    w->setElement(temp);
}
//This method deletes the node with the given element, if the node exists,
// returning the last node involved in the procedure.If the node
//doesn't exists, it returns NULL and the process is canceled.
AVL::Node* AVL::deleteItem(string key){
    if(size == 0){
        return(NULL);
    }

    AVL::Node* delNode = findNode(key, this->root);
    string keyNode = delNode->getElement();
    //element doesn't exist
    if(key != keyNode)
        return(NULL);
    
    else{
        //one of the nodes child is null
        AVL::Node* returnNode;
        if((delNode->getLeft() == NULL) || (delNode->getRight() == NULL)){
            
            returnNode = (delNode->getLeft() != NULL ? delNode->getLeft():
            delNode->getRight() != NULL ? delNode->getRight(): delNode->getParent());
            
            deleteNode(delNode);
            return(returnNode);
        }

        else{//both childs are not null, so we have to find the node with the lexicographically 
            //smallest element value in the nodes
            //left subtree
            AVL::Node* cursor = delNode->getRight();
            AVL::Node* temp;
            AVL::Node* parentDelNode;

            while((temp = cursor->getLeft()) != NULL){
                cursor = temp;
            }

            exchange(cursor, delNode);
            parentDelNode = cursor->getParent();
            deleteNode(cursor);
            
            return(parentDelNode);
        }
    }
}
//This method deletes the node with the given string as his element.If the node exists, 
//it will be exchanged with the node with the smallest
//value from the nodes right subtree.Once the procedure is finished, it rebalances the AVL tree.
//If the node exists and has been deleted sucessfully, the method returns true, else the procedure 
//will be canceled nad false will be returned
bool AVL::rmv(string str){
    AVL::Node* delNode = deleteItem(str);
    
    if(delNode == NULL)
        return(false);

    rebalance(delNode);
    return(true);
}

//This method is used to construct a new AVL tree, who's contents are the contents
// of the given root of the existing AVL tree, based on
//its pre-order traversal 
void AVL::preorderTreaversal(AVL::Node* node, AVL& tree){ 
    
    if(node == NULL)
        return;
    
    tree.add(node->getElement());

    preorderTreaversal(node->getLeft(), tree);
    
    preorderTreaversal(node->getRight(), tree);   
}
//This friend function is adding the elements of each node involved in the AVL tree,
// based on pre-order traversal
void preorderOut(AVL::Node* node, string& buffer){
    if (node == NULL) 
        return; 
  
    buffer.append(node->getElement());
    buffer.append(" ");
  
    /* then recur on left subtree */
    preorderOut(node->getLeft(), buffer);  
  
    /* now recur on right subtree */
    preorderOut(node->getRight(), buffer); 
}

//This method inserts to the given ofstream the contents of the dot file 
//while it's traversing the existing AVL tree
void AVL::toDotTraversal(AVL::Node* currentNode, ofstream& myfile){
    hash<AVL::Node*> hash_node;
    
    myfile << hash_node(currentNode);
    myfile << " [label=\"" << currentNode->getElement();
    myfile << "\" ,shape=circle, color=";
    
    if(currentNode->getLeft() == NULL && currentNode->getRight() == NULL)
        myfile << "red]\n";
    else
        myfile << "black]\n";
    
    if(currentNode->getLeft() != NULL){
        myfile << hash_node(currentNode) << " -- ";
        myfile << hash_node(currentNode->getLeft()) << endl;
        toDotTraversal(currentNode->getLeft(), myfile);
    }
    if(currentNode->getRight() != NULL){
        myfile << hash_node(currentNode) << " -- ";
        myfile << hash_node(currentNode->getRight()) << endl;
        toDotTraversal(currentNode->getRight(), myfile);
    }
}

//This method creates the dot file with the given name as filename
void AVL::print2DotFile(char* filename){
    ofstream myfile;

    myfile.open (filename);
    myfile<<"graph AVL {\n";
    toDotTraversal(root, myfile);
    myfile<<"}";
    myfile.close();
}

//This method is traversing the existing AVL tree accordiing to the pre-order traversal
void AVL::pre_order(ostream& out){
    string preOrderBuffer;
   
    preorderOut(root, preOrderBuffer);
    out<<preOrderBuffer;
}
//This constructor creates an empty AVL tree
AVL::AVL(){
    root = NULL;
    size = 0;
}

//Copy Constructor
AVL::AVL(const AVL& avlTree){
    size = 0;
    preorderTreaversal(avlTree.root, *this);
}
//This friend function overloads the operator <<.It prints 
//to the given ostream the contents of the existing AVL tree based on it's
//pre-order traversal
ostream& operator<<(ostream& out, const AVL& tree){
    string preOrderBuffer;
    preorderOut(tree.root, preOrderBuffer);

    out<<preOrderBuffer;
    return(out);
}
//Copy constructor
AVL::Iterator::Iterator(const Iterator& it){
    *this = it;
}

//This constructor is creating an Iterator who's stack contents initially
// are a NULL value and the root if the existing AVL tree
AVL::Iterator::Iterator(AVL::Node* root){
        nodeStack.push(NULL);
        nodeStack.push(root);
}
//This constructor is creating an iterator Who's stack content initially is the NULL value
AVL::Iterator::Iterator(){
    nodeStack.push(NULL);
}
//This method is moving the iterator to the next element 
//of the existing avl tree based on its pre order traversal,It also returns
//a reference to the current iterator
AVL::Iterator& AVL::Iterator:: operator++(){

        AVL::Node *popNode = nodeStack.top();
        nodeStack.pop();

        if(popNode->getRight() != NULL)
            nodeStack.push(popNode->getRight());
        if(popNode->getLeft() != NULL)
            nodeStack.push(popNode->getLeft());

        return(*this);
}
//This method is moving the iterator to the next element of the existing avl tree 
//based on its pre order traversal,It also returns
//a reference to the previous iterator
AVL::Iterator AVL::Iterator:: operator++(int a){
    
    Iterator it(*this);
    ++(*this);
    return(it);  
}
//This method returns the element of the node pointed by the iterator
string AVL::Iterator:: operator*(){
    return(nodeStack.top()->getElement());
}
//This method examines whether the Iterator who's placed on the left side of the operator isn't pointing 
//at the same node as the right side Iterator.
bool AVL::Iterator:: operator!=(Iterator it){
    if(this->nodeStack.top() == it.nodeStack.top())
        return(false);
    
    return(true);
}
//This method examines whether the Iterator who's placed on the left side of the operator points 
//at the same node as the right side Iterator.
bool AVL::Iterator:: operator==(Iterator it){
   if(this->nodeStack.top() != it.nodeStack.top())
        return(false);
    
    return(true);
}
//This method returns an Iterator pointing at the first element of the existing AVL tree
//based on it's pre-order traversal
AVL::Iterator AVL::begin() const{
    AVL::Iterator it = Iterator(root);
    return(it);
}
//This method returns an Iterator pointing at the last element of the existing AVL tree
//based on it's pre-order traversal
AVL::Iterator AVL::end() const{
    AVL::Iterator it = Iterator();
    return(it);
}
//The AVL tree placed on the left side of the operator will contain the same elements
//with the tree placed on the right side of the operator.The contents will be inserted based
//on the right side tree pre-order traversal
AVL& AVL:: operator =(const AVL& avl){
    if(root != NULL)
        while(deleteItem(this->root->getElement()) != NULL);
    
    root = NULL;
    size=0;
    preorderTreaversal(avl.root, *this);
    return(*this);
}
//This method creates a new AVL tree which contains all the elements of avl tree placed on the 
//left side of the operator, based on it's pre-order traversal and the insertion of the right-side
//tree contents to the left side tree, based on the right side tree pre order traversal.
AVL AVL:: operator +(const AVL& avl){
    AVL avl3 = AVL();

    avl3 = *this;
    preorderTreaversal(avl.root, avl3);

    return(avl3);
}
//This method adds the contents of the tree placed on the right side of the operator,
//to the left side tree based on the right side tree pre order traversal
AVL& AVL::operator+=(const AVL& avl){
    preorderTreaversal(avl.root, *this);
    return(*this);
}
//This method adds to the avl tree the element on the right side of the operator
AVL& AVL::operator+=(const string& e){
    this->add(e);
    return(*this);
}
//This method removes from the avl tree the element on the right side of the operator
AVL& AVL::operator-=(const string& e){
    this->rmv(e);
    return(*this);
}
//This method creates a new avl tree whos contents are the contents of the avl tree
//placed on the left side of the operator, plus the element given on the right side
AVL AVL::operator +(const string& e){
    AVL newAvl = AVL();
    preorderTreaversal(this->root, newAvl);
    newAvl+=e;
    return(newAvl);
}
//This method creates a new avl tree whos contents are the contents of the avl tree
//placed on the left side of the operator, minus the element given on the right side
AVL AVL::operator -(const string& e){
    AVL newAvl = AVL();
    preorderTreaversal(this->root, newAvl);
    newAvl-=e;
    return(newAvl);
}
