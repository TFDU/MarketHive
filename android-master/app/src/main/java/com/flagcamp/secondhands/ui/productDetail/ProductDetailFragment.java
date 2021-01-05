package com.flagcamp.secondhands.ui.productDetail;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.flagcamp.secondhands.CurrentUserSingleton;
import com.flagcamp.secondhands.R;
import com.flagcamp.secondhands.databinding.FragmentProductDetailBinding;
import com.flagcamp.secondhands.model.ChatRoom;
import com.flagcamp.secondhands.model.Image;
import com.flagcamp.secondhands.model.Product;
import com.flagcamp.secondhands.model.User;
import com.flagcamp.secondhands.network.Api;
import com.flagcamp.secondhands.repository.ProductRepository;
import com.flagcamp.secondhands.repository.ProductViewModelFactory;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class ProductDetailFragment extends Fragment {
    private Api api;
    private FirebaseFirestore database;
    private static int CATAGORIES = 5;

    private FragmentProductDetailBinding binding;

    private ProductDetailViewModel viewModel;
    private StaggeredGridLayoutManager layoutManager;
    private CurrentUserSingleton currentUser;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        // Inflate the layout for this fragment
        binding = FragmentProductDetailBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        currentUser = CurrentUserSingleton.getInstance();
        Product product = ProductDetailFragmentArgs.fromBundle(getArguments()).getProduct();
        if(product.favorite == true){
            binding.productDetailAddToFavoriteButton.setText("Saved!");
        }else{
            binding.productDetailAddToFavoriteButton.setText("Save");
        }
        binding.productDetailRequestToBuyButton.setOnClickListener(new View.OnClickListener(){
           public void onClick(View v){
               viewModel.setIdToken(CurrentUserSingleton.getInstance().getIdToken());
               viewModel.setProductId(product.productId);
               viewModel.createRequest()
                       .observe(getViewLifecycleOwner(),
                               createRequestResponse -> {
                                   if (createRequestResponse != null && createRequestResponse.status.equals("OK")) {
                                       Toast.makeText(getContext(), "requested", Toast.LENGTH_LONG).show();
                                   } else {
                                       Toast.makeText(getContext(), "Something Wrong, Please Try Again Later", Toast.LENGTH_LONG).show();
                                   }

                               });
           }
        });
        binding.productDetailAddToFavoriteButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                viewModel.setIdToken(CurrentUserSingleton.getInstance().getIdToken());
                viewModel.setProductId(product.productId);
                if(!product.favorite){
                    viewModel.addFavProduct()
                            .observe(
                                    getViewLifecycleOwner(),
                                    updateFavResponse -> {
                                        if (updateFavResponse != null && updateFavResponse.status.equals("OK")) {
                                            Toast.makeText(getContext(), "Like", Toast.LENGTH_LONG).show();
                                            binding.productDetailAddToFavoriteButton.setText("Saved!");
                                        } else {
                                            Toast.makeText(getContext(), "Something Wrong, Please Try Again Later", Toast.LENGTH_LONG).show();
                                        }
                                    }
                            );
                }else{
                    viewModel.deleteFavProduct().observe(
                            getViewLifecycleOwner(),
                            deleteFavResponse -> {
                                if ("OK".equals(deleteFavResponse.status)) {
                                    Toast.makeText(getContext(), "No longer like", Toast.LENGTH_SHORT).show();
                                    binding.productDetailAddToFavoriteButton.setText("Save");
                                } else {
                                    Toast.makeText(getContext(), "Something went wrong, please try later", Toast.LENGTH_SHORT).show();
                                }
                                //re-render fav list
//                                getProducts();
                            }

                    );
                    binding.productDetailAddToFavoriteButton.setText("Save");
                }
                product.favorite = !product.favorite;
            }
        });


        //binding.productDetailChatButton.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_detail_to_navigation_profile, null));
        binding.productDetailChatButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                User user = product.user; // TODO: fetch user data
                if(currentUser.getUserId().equals(user.userUID)){
                    Toast.makeText(getContext(),"Cannot chat with yourself!", Toast.LENGTH_LONG).show();
                    return;
                }
                addFriendship(user);
                ProductDetailFragmentDirections.ActionNavigationDetailToNavigationMessage action = ProductDetailFragmentDirections.actionNavigationDetailToNavigationMessage(user);
                Navigation.findNavController(view).navigate(action);

            }
        });
        product.availability = true;
        if(!product.availability){
            binding.productDetailAddToFavoriteButton.setVisibility(View.INVISIBLE);
            binding.productDetailRequestToBuyButton.setVisibility(View.INVISIBLE);
            binding.productDetailChatButton.setVisibility(View.INVISIBLE);
            binding.productDetailNotAvailableView.setVisibility(View.VISIBLE);
        }else{
            binding.productDetailAddToFavoriteButton.setVisibility(View.VISIBLE);
            binding.productDetailRequestToBuyButton.setVisibility(View.VISIBLE);
            binding.productDetailChatButton.setVisibility(View.VISIBLE);
            binding.productDetailNotAvailableView.setVisibility(View.INVISIBLE);
        }
        binding.productDetailNotAvailableView.setTextSize(18);
        binding.productDetailNotAvailableView.setText("This product is no longer available");
        binding.productDetailPriceView.setText("$" + product.price);
        binding.productDetailDiscriptionTitleView.setText("Description");
        binding.productDetailDiscriptionTitleView.setTextSize(17);
        binding.productDetailDiscriptionContentView.setText(product.description);
        binding.productDetailSellerNameView.setText(product.user.name);
        binding.productDetailSellerRatingView.setText(product.user.rating);
        binding.productDetailLocationView.setText("location:" + product.city + product.state);
        binding.productDetailPostedAtView.setText("posted at: " + product.timestamp + product.timezoneId);

        // dummy image
        product.user.photoUrl = "https://firebasestorage.googleapis.com/v0/b/androidsecondhandmarket.appspot.com/o/testImgs_v1%2FTrump5.jpg?alt=media&token=4978e837-4d5e-4794-830f-dc15e8ad198b";

        Picasso.get().load(product.user.photoUrl).into(binding.productDetailSellerImageView);
        binding.productDetailSellerImageView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                //TODO: connect to user profile.
                User user = product.user;
                if(currentUser.getUserId().equals(user.userUID)){
                    Toast.makeText(getContext(),"This is yourself!", Toast.LENGTH_LONG).show();
                    return;
                }
                ProductDetailFragmentDirections.ActionNavigationDetailToNavigationVisitorViewProfile action = ProductDetailFragmentDirections.actionNavigationDetailToNavigationVisitorViewProfile(product.user);
                Navigation.findNavController(view).navigate(action);
            }
        });
        binding.productDetailTitleView.setText(product.productName);
        binding.productDetailTitleView.setTextSize(18);
        binding.productDetailChatButton.setText("Chat");
        binding.productDetailRequestToBuyButton.setText("Request");


        ProductDetailAdapter productAdapter = new ProductDetailAdapter();
        layoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL);

        binding.productDetailImageRecyclerView.setLayoutManager(layoutManager);
        binding.productDetailImageRecyclerView.setAdapter(productAdapter);
        List<Image> imageList = new ArrayList<>();
        // dummy images since there are no valid imageUrls in json
//        String dummyImages = "https://firebasestorage.googleapis.com/v0/b/androidsecondhandmarket.appspot.com/o/testImgs_v1%2FTrump1.jpg?alt=media&token=a7da5dad-2354-4979-b155-75fb1e9e845d*https://firebasestorage.googleapis.com/v0/b/androidsecondhandmarket.appspot.com/o/testImgs_v1%2FTrump2.jpg?alt=media&token=d4eccd9e-678a-49f3-abd8-739fa62ed275*https://firebasestorage.googleapis.com/v0/b/androidsecondhandmarket.appspot.com/o/testImgs_v1%2FTrump3.jpg?alt=media&token=50d33d62-d9e2-42b7-9084-62743b06fb5b";
//        product.imageUrls = dummyImages;
        if(product.imageUrls != null){
            String[] images = product.imageUrls.split("\\*");
            for(String s : images){ // TODO 1.confirm data format with backend. 2.split and add images
                imageList.add(new Image(s));
            }
        }

        ProductRepository repository = new ProductRepository(requireContext());
        viewModel = new ViewModelProvider(this, new ProductViewModelFactory(repository)).get(ProductDetailViewModel.class);
        viewModel.setSearchInput("us");
        viewModel.getProducts().observe(getViewLifecycleOwner(),productResponse -> {
            if(productResponse != null){
                productAdapter.setImages(imageList);

            }

        });



    }

    private void addFriendship(User user) {
        database = FirebaseFirestore.getInstance();
        CurrentUserSingleton currentUser = CurrentUserSingleton.getInstance();
        DocumentReference friendshipRef = database.collection("chatRooms").document("friendship");
        ChatRoom chatRoom1 = new ChatRoom(user.userUID, user.name);
        ChatRoom chatRoom2 = new ChatRoom(currentUser.getUserId(), currentUser.getUserName());
        friendshipRef.collection(currentUser.getUserId()).document(user.userUID).set(chatRoom1);
        friendshipRef.collection(user.userUID).document(currentUser.getUserId()).set(chatRoom2);
    }


}
