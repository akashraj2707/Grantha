package Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grantha.CommentActivity;
import com.example.grantha.R;
import com.google.common.collect.BiMap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hendraanggrian.appcompat.widget.SocialTextView;
import com.squareup.picasso.Picasso;

import java.util.List;

import Model.Post;
import Model.User;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private Context mContext;
    private List<Post> mPosts;

    private FirebaseUser firebaseUser;

    public PostAdapter(Context mContext, List<Post> mPosts) {
        this.mContext = mContext;
        this.mPosts = mPosts;
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.post_item, parent,false);
        return new PostAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Post post=mPosts.get(position);

        Picasso.get().load(post.getImageUrl()).into(holder.postImage);
        holder.title.setText(post.getTitle());
        String html_to_style= post.getArticle().toString();
        holder.webArticle.loadDataWithBaseURL(null,html_to_style,"text/html","utf-8",null);




        FirebaseDatabase.getInstance().getReference().child("Users").child(post.getPublisher()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=snapshot.getValue(User.class);
                if(user.getImageurl().equals("default")){
                    holder.profile_image.setImageResource(R.mipmap.ic_launcher);
                }else{
                    Picasso.get().load(post.getImageUrl()).into(holder.profile_image);
                }

                holder.username.setText(user.getUsername());
                holder.author.setText(user.getName());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //add a count in  view
        FirebaseDatabase.getInstance().getReference().child("Views").child(post.getPostId()).push().setValue(firebaseUser.getUid());


        //set colored heart if liked else hollow
        isLiked(post.getPostId(),holder.like);
        //for dislikes
        isDisliked(post.getPostId(),holder.dislike);
        //for bookmarks
        isBookmarked(post.getPostId(),holder.save);
        //display no of likes
        noOfLikes(post.getPostId(),holder.noOfLikes);
        noOfComments(post.getPostId(),holder.noOfComments);
        noOfDislikes(post.getPostId(),holder.noOfDislikes);
        noOfViews(post.getPostId(),holder.noOfViews);

        //bookmarks feature
        holder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.save.getTag().equals("save")){
                    FirebaseDatabase.getInstance().getReference().child("Bookmarks").child(firebaseUser.getUid())
                            .child(post.getPostId()).setValue(true);
                }else{
                    FirebaseDatabase.getInstance().getReference().child("Bookmarks").child(firebaseUser.getUid())
                            .child(post.getPostId()).removeValue();
                }
            }
        });

        //like features
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.like.getTag().equals("like")) {
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getPostId())
                            .child(firebaseUser.getUid()).setValue(true);

                } else {
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getPostId())
                            .child(firebaseUser.getUid()).removeValue();

                }
            }

        });
        //dislike feature
        holder.dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.dislike.getTag().equals("dislike")){
                    FirebaseDatabase.getInstance().getReference().child("Dislikes").child(post.getPostId())
                            .child(firebaseUser.getUid()).setValue(true);
                }else{
                    FirebaseDatabase.getInstance().getReference().child("Dislikes").child(post.getPostId())
                            .child(firebaseUser.getUid()).removeValue();
                    holder.dislike.setTag("dislike");
                }
            }
        });
        //directed to comment activity

        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, CommentActivity.class);
                intent.putExtra("postId",post.getPostId());
                intent.putExtra("authorId",post.getPublisher());
                mContext.startActivity(intent);
            }
        });

        holder.noOfComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, CommentActivity.class);
                intent.putExtra("postId",post.getPostId());
                intent.putExtra("authorId",post.getPublisher());
                mContext.startActivity(intent);

            }
        });







    }


    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView profile_image;
        public TextView username;
        public ImageView postImage;
        public ImageView like;
        public  ImageView comment;
        public ImageView share;
        public ImageView save;
        public ImageView dislike;

        public TextView noOfLikes;
        public TextView noOfDislikes;
        public WebView webArticle;
        public TextView noOfComments;
        public TextView noOfViews;
        public TextView title;
        public TextView author;
        //SocialTextView description;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profile_image=itemView.findViewById(R.id.profile_image);
            username=itemView.findViewById(R.id.username);
            postImage=itemView.findViewById(R.id.post_image);
            like=itemView.findViewById(R.id.like);
            comment=itemView.findViewById(R.id.comment);
            share=itemView.findViewById(R.id.share);
            title=itemView.findViewById(R.id.title);
            dislike=itemView.findViewById(R.id.dislike);

            save=itemView.findViewById(R.id.save);
            noOfLikes=itemView.findViewById(R.id.no_of_likes);
            noOfDislikes=itemView.findViewById(R.id.no_of_dislikes);
            noOfComments=itemView.findViewById(R.id.no_of_comments);
            noOfViews=itemView.findViewById(R.id.no_of_views);
            webArticle=itemView.findViewById(R.id.web);
            author=itemView.findViewById(R.id.author);

        }
    }

    //to check no of comments
    private void noOfComments(String postId, final TextView text)
    {
        FirebaseDatabase.getInstance().getReference().child("Comments").child(postId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        text.setText("View all "+snapshot.getChildrenCount()+" comments");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    //to check no of views
    private void noOfViews(String postId,final TextView text)
    {
        FirebaseDatabase.getInstance().getReference().child("Views").child(postId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        text.setText(snapshot.getChildrenCount()+" views");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    //to check no of likes
    private void noOfLikes(String postId, final TextView text) {
        FirebaseDatabase.getInstance().getReference().child("Likes").child(postId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        text.setText(snapshot.getChildrenCount()+" upVotes");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    private void noOfDislikes(String postId,final TextView text){
        FirebaseDatabase.getInstance().getReference().child("Dislikes").child(postId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        text.setText(snapshot.getChildrenCount()+" downVotes");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    private void isDisliked(String postId, final ImageView imageView) {
        FirebaseDatabase.getInstance().getReference().child("Dislikes").child(postId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child(firebaseUser.getUid()).exists()){
                            imageView.setImageResource(R.drawable.ic_disliked);
                            imageView.setTag("disliked");
                        }else{
                            imageView.setImageResource(R.drawable.ic_dislike);
                            imageView.setTag("dislike");

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
    private void isBookmarked(final String postId, final ImageView imageView) {
        FirebaseDatabase.getInstance().getReference().child("Bookmarks").child(firebaseUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child(postId).exists()){
                            imageView.setImageResource(R.drawable.ic_saved);
                            imageView.setTag("saved");
                        }else{
                            imageView.setImageResource(R.drawable.ic_save);
                            imageView.setTag("save");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }





    private void isLiked(String postId,final ImageView imageView)
    {
        FirebaseDatabase.getInstance().getReference().child("Likes").child(postId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.child(firebaseUser.getUid()).exists()) {
                            imageView.setImageResource(R.drawable.ic_liked);


                            imageView.setTag("liked");

                        } else {
                            imageView.setImageResource(R.drawable.ic_like);
                            imageView.setTag("like");

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


}
