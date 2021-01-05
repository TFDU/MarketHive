package com.flagcamp.secondhands.ui.post;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.flagcamp.secondhands.CurrentUserSingleton;
import com.flagcamp.secondhands.R;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;
import com.flagcamp.secondhands.databinding.FragmentPostBinding;
import com.flagcamp.secondhands.model.Product;
import com.flagcamp.secondhands.model.User;
import com.flagcamp.secondhands.repository.ProductRepository;
import com.flagcamp.secondhands.repository.ProductViewModelFactory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class PostFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri; // Product image URL;
    private StorageReference mStorageRef;
    private StorageTask uploadTask;
    private StorageReference mProductRef;
    private PostViewModel viewModel;
    private FragmentPostBinding binding;
    private Product product;
    private String category;
    private String state;
    private double latitude;
    private double longitude;
    ArrayList<Uri> ImageList = new ArrayList<>();
    private Button prevBtn, nextBtn, deleteAll;
    private List<String> imagesUrl = new ArrayList<>();
    private StringBuilder sb = new StringBuilder();
    private String uris;
    int position = 0;
    private String idToken;
    private int count;


    public PostFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPostBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpCategory();
        setUpLocation();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mProductRef = mStorageRef.child("products");
        binding.buttonChooseProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "please select less than 9 pics", Toast.LENGTH_SHORT).show();
                openImageChooser();
            }
        });

        binding.buttonPostProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uploadTask != null && uploadTask.isInProgress()) {
                    Toast.makeText(getContext(), "Post in progress", Toast.LENGTH_SHORT).show();
                } else {
                    postProduct();
                    binding.imageView.setImageDrawable(null);
                    binding.productTitle.getText().clear();
                    binding.productPrice.getText().clear();
                    binding.productDescription.getText().clear();
                    binding.productLocationSpinner.setSelection(0);
                    binding.productCategorySpinner.setSelection(0);
                }
            }
        });
        binding.deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ImageList.size() > 0) {
                    ImageList.clear();
                    binding.imageView.setImageDrawable(null);
                    binding.productTitle.getText().clear();
                    binding.productPrice.getText().clear();
                    binding.productDescription.getText().clear();
                    binding.productLocationSpinner.setSelection(0);
                    binding.productCategorySpinner.setSelection(0);
                    Toast.makeText(getContext(), "Delete Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "No photo found", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // click handle the previous photo
        binding.previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position > 0 && ImageList.size() > 0) {
                    position--;
                    Picasso.get().load(ImageList.get(position)).into(binding.imageView);
                } else {
                    Toast.makeText(getContext(), "No previous photo", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // click handle the next photo
        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ImageList.size() > 0 && position < ImageList.size() - 1) {
                    position++;
                    Picasso.get().load(ImageList.get(position)).into(binding.imageView);
                } else {
                    Toast.makeText(getContext(), "No next photo", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int currentImageSelect = 0;
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK
                && data != null && data.getData() != null) {
            if (data.getClipData() != null) {
                int countClipData = data.getClipData().getItemCount();
                while (currentImageSelect < countClipData) {
                    mImageUri = data.getClipData().getItemAt(currentImageSelect).getUri();
                    ImageList.add(mImageUri);
                    currentImageSelect = currentImageSelect + 1;
                    if (ImageList.size() > 9) {
                        ImageList.clear();
                        binding.imageView.setImageDrawable(null);
                        Toast.makeText(getContext(), "please select at most 9", Toast.LENGTH_SHORT).show();
                        break;
                    }

                }
                if (ImageList.size() > 0) {
                    Picasso.get().load(ImageList.get(0)).into(binding.imageView);
                }
            } else {
                Uri mImageUri = data.getData();
                ImageList.add(mImageUri);
                Picasso.get().load(ImageList.get(0)).into(binding.imageView);
            }
            position = 0;
        }

    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void postProduct() {
        product = new Product();

        if (mImageUri == null) {
            Toast.makeText(getContext(), "Please upload your product image", Toast.LENGTH_SHORT).show();
        } else if (binding.productTitle.getText().toString().matches("")) {
            Toast.makeText(getContext(), "Please enter your product title", Toast.LENGTH_SHORT).show();
        } else if (binding.productPrice.getText().toString().matches("")) {
            Toast.makeText(getContext(), "Please enter your product price", Toast.LENGTH_SHORT).show();
        } else {
            CurrentUserSingleton currentUser = CurrentUserSingleton.getInstance();
            product.user = new User();
            product.user.userUID = currentUser.getUserId(); // TODO: for real
//            product.user.userUID = "user1"; // For test
            product.productName = binding.productTitle.getText().toString();
            product.description = binding.productDescription.getText().toString();
            product.price = binding.productPrice.getText().toString();

            initLocation();
            product.lat = latitude;
            product.lon = longitude;

            product.category = category;
            product.state = state;

            FirebaseAuth.getInstance().getCurrentUser().getIdToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                viewModel.setIdToken(task.getResult().getToken());
                            }
                        }
                    });

            ProductRepository repository = new ProductRepository(requireContext());
            viewModel = new ViewModelProvider(this, new ProductViewModelFactory(repository)).get(PostViewModel.class);

            for (int i = 0; i < ImageList.size(); i++) {
                long time = System.currentTimeMillis();
                Log.d("onTest1", "testURL " + time + "." + getFileExtension(ImageList.get(i)));
                StorageReference fileReference = mProductRef.child(time + "." + getFileExtension(ImageList.get(i)));        // THIS IS GOOGLE COULD REFERENCE
                uploadTask = fileReference.putFile(ImageList.get(i));
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                if (sb.length() != 0) {
                                    sb.append("*");
                                }
                                sb.append(uri.toString());
                                Log.d("ImageUrls: ", sb.toString());
                                count++;
                                if (count == ImageList.size()) {
                                    product.imageUrls = sb.toString();
                                    viewModel.setProduct(product);
                                    viewModel.postProduct()
                                            .observe(
                                                    getViewLifecycleOwner(),
                                                    postProductResponse -> {
                                                        if (postProductResponse != null && postProductResponse.status.equals("OK")) {
                                                            Toast.makeText(getContext(), "Post Successful", Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                            );
                                    count = 0;
                                    sb = new StringBuilder();
                                    ImageList.clear();
                                }
                            }
                        });
                    }
                });

                Log.d("onSuccess", "onSuccess: sb= " + sb.toString());

            }
        }
    }

    @SuppressLint("MissingPermission")
    private void initLocation() {
        //permission deny, give a default location
        if (Build.VERSION.SDK_INT >= 23
                && getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationManager lm =
                    (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            //here must use list<string> in case one provider is null
            List<String> providers = lm.getProviders(true);
            Location location = null;
            for (String provider : providers) {
                Location l = lm.getLastKnownLocation(provider);
                if (l == null) {
                    continue;
                }
                if (location == null || l.getAccuracy() < location.getAccuracy()) {
                    // Found best last known location: %s", l);
                    location = l;
                }
            }
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }
    }

    private void setUpCategory() {
        ArrayAdapter<CharSequence> categorySpinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.category, android.R.layout.simple_spinner_item);
        categorySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.productCategorySpinner.setAdapter(categorySpinnerAdapter);
        binding.productCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setUpLocation() {
        ArrayAdapter<CharSequence> locationSpinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.location, android.R.layout.simple_spinner_item);
        locationSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.productLocationSpinner.setAdapter(locationSpinnerAdapter);
        binding.productLocationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                state = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
